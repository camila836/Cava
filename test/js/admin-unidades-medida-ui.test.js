'use strict';

const assert = require('node:assert/strict');
const fs = require('node:fs');
const vm = require('node:vm');

class ClassList {
  constructor(element) { this.element = element; }
  toggle(name, force) {
    const classes = new Set(this.element.className.split(/\s+/).filter(Boolean));
    const enabled = force === undefined ? !classes.has(name) : force;
    if (enabled) classes.add(name); else classes.delete(name);
    this.element.className = [...classes].join(' ');
  }
}

class Element {
  constructor(tagName, id = '') {
    this.tagName = tagName.toUpperCase();
    this.id = id;
    this.dataset = {};
    this.children = [];
    this.listeners = new Map();
    this.hidden = false;
    this.disabled = false;
    this.textContent = '';
    this.value = '';
    this.className = '';
    this.classList = new ClassList(this);
    this.attributes = new Map();
    this.open = false;
  }
  setAttribute(name, value) { this.attributes.set(name, String(value)); }
  append(...children) { this.children.push(...children); }
  appendChild(child) { this.children.push(child); return child; }
  replaceChildren(...children) { this.children = [...children]; }
  addEventListener(type, listener) {
    if (!this.listeners.has(type)) this.listeners.set(type, []);
    this.listeners.get(type).push(listener);
  }
  async dispatch(type, event = {}) {
    const values = (this.listeners.get(type) || []).map(listener => listener(event));
    await Promise.all(values);
  }
  closest(selector) {
    return selector === 'button[data-action]' && this.tagName === 'BUTTON'
      && this.dataset.action ? this : null;
  }
  focus() {}
  reset() {}
  showModal() { this.open = true; }
  close() { this.open = false; void this.dispatch('close'); }
}

const ids = [
  'unidades-app', 'unidad-form', 'unidad-id', 'unidad-descripcion',
  'descripcion-error', 'form-title', 'save-button', 'cancel-edit',
  'reload-button', 'global-message', 'loading-state', 'empty-state',
  'table-wrap', 'unidades-body', 'delete-dialog', 'delete-name',
  'confirm-delete'
];
const elements = new Map(ids.map(id => [id, new Element('div', id)]));
elements.get('unidades-app').dataset.endpoint = '/Cava/admin/unidades-medida';
elements.get('unidad-form').tagName = 'FORM';
elements.get('unidad-id').tagName = 'INPUT';
elements.get('unidad-descripcion').tagName = 'INPUT';
elements.get('save-button').tagName = 'BUTTON';
elements.get('cancel-edit').tagName = 'BUTTON';
elements.get('reload-button').tagName = 'BUTTON';
elements.get('unidades-body').tagName = 'TBODY';
elements.get('delete-dialog').tagName = 'DIALOG';
elements.get('confirm-delete').tagName = 'BUTTON';

const calls = [];
let stored = [];
const response = body => ({
  ok: true,
  redirected: false,
  url: 'http://localhost/Cava/admin/unidades-medida',
  headers: { get: name => name.toLowerCase() === 'content-type'
    ? 'application/json;charset=UTF-8' : null },
  json: async () => body
});
const fetch = async (target, options) => {
  calls.push({ target, options });
  if (options.method === 'GET') {
    return response({ ok: true, message: 'Listado.', data: [...stored], errors: {} });
  }
  if (options.method === 'POST') {
    const payload = JSON.parse(options.body);
    stored = [{ id: 1, descripcion: payload.descripcion }];
    return response({ ok: true, message: 'Creada.', data: stored[0], errors: {} });
  }
  if (options.method === 'PUT') {
    const payload = JSON.parse(options.body);
    stored = [{ id: payload.id, descripcion: payload.descripcion }];
    return response({ ok: true, message: 'Actualizada.', data: stored[0], errors: {} });
  }
  if (options.method === 'DELETE') {
    assert.equal(target, '/Cava/admin/unidades-medida?id=1');
    assert.equal(options.body, undefined);
    assert.equal(options.headers['Content-Type'], undefined);
    assert.equal(options.headers['X-CSRF-Token'], 'csrf-test');
    stored = [];
    return response({ ok: true, message: 'Eliminada.', data: { id: 1 }, errors: {} });
  }
  throw new Error(`Metodo inesperado: ${options.method}`);
};

const document = {
  getElementById: id => elements.get(id) || null,
  querySelector: selector => selector === 'meta[name="csrf-token"]'
    ? { content: 'csrf-test' } : null,
  createElement: tagName => new Element(tagName)
};
const window = { location: { assign() { throw new Error('Redireccion inesperada'); } } };
const source = fs.readFileSync('web/js/admin-unidades-medida.js', 'utf8');
vm.runInNewContext(source, { document, window, fetch, Map, Number, String,
  Array, Error, encodeURIComponent });

const settle = () => new Promise(resolve => setImmediate(resolve));

(async () => {
  await settle();
  assert.equal(elements.get('empty-state').hidden, false);
  assert.equal(elements.get('table-wrap').hidden, true);

  const special = "F9 UI <script>&'";
  elements.get('unidad-descripcion').value = special;
  await elements.get('unidad-form').dispatch('submit', { preventDefault() {} });
  assert.equal(elements.get('unidades-body').children.length, 1);
  assert.equal(elements.get('unidades-body').children[0].children[0].textContent, special);
  assert.equal(elements.get('global-message').textContent, 'Creada.');

  let actionCell = elements.get('unidades-body').children[0].children[1];
  const editButton = actionCell.children[0];
  await elements.get('unidades-body').dispatch('click', { target: editButton });
  assert.equal(elements.get('form-title').textContent, 'Editar unidad');
  assert.equal(elements.get('unidad-descripcion').value, special);

  elements.get('unidad-descripcion').value = 'F9 UI editada';
  await elements.get('unidad-form').dispatch('submit', { preventDefault() {} });
  assert.equal(elements.get('unidades-body').children[0].children[0].textContent,
    'F9 UI editada');

  actionCell = elements.get('unidades-body').children[0].children[1];
  const deleteButton = actionCell.children[1];
  await elements.get('unidades-body').dispatch('click', { target: deleteButton });
  assert.equal(elements.get('delete-dialog').open, true);
  assert.equal(elements.get('delete-name').textContent, 'F9 UI editada');
  await elements.get('confirm-delete').dispatch('click');
  assert.equal(elements.get('unidades-body').children.length, 0);
  assert.equal(elements.get('empty-state').hidden, false);
  assert.equal(elements.get('global-message').textContent, 'Eliminada.');
  assert.deepEqual(calls.map(call => call.options.method), ['GET', 'POST', 'PUT', 'DELETE']);
  console.log('UI DOM PASS: listar, crear, editar, eliminar y render seguro');
})().catch(error => {
  console.error(error);
  process.exitCode = 1;
});
