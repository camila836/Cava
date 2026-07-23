'use strict';

const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const vm = require('node:vm');

class MockElement {
  constructor() {
    this.attributes = new Map();
    this.children = [];
    this.listeners = new Map();
    this.queries = new Map();
    this.queryLists = new Map();
    this.style = {};
    this.className = '';
    this.disabled = false;
    this.tabIndex = 0;
    this.textContent = '';
    this.width = 0;
    this.classList = {
      toggle: () => {}
    };
  }

  addEventListener(type, listener) {
    const listeners = this.listeners.get(type) || [];
    listeners.push(listener);
    this.listeners.set(type, listeners);
  }

  dispatch(type, values = {}) {
    const event = {
      key: '',
      clientX: 0,
      defaultPrevented: false,
      preventDefault() {
        this.defaultPrevented = true;
      },
      ...values
    };
    (this.listeners.get(type) || []).forEach((listener) => listener(event));
    return event;
  }

  setAttribute(name, value) {
    this.attributes.set(name, String(value));
  }

  getAttribute(name) {
    return this.attributes.has(name) ? this.attributes.get(name) : null;
  }

  removeAttribute(name) {
    this.attributes.delete(name);
  }

  querySelector(selector) {
    return this.queries.get(selector) || null;
  }

  querySelectorAll(selector) {
    return this.queryLists.get(selector) || [];
  }

  appendChild(child) {
    this.children.push(child);
    return child;
  }

  replaceChildren() {
    this.children = [];
  }

  getBoundingClientRect() {
    return {width: this.width};
  }

  focus() {}
}

const header = new MockElement();
const toggle = new MockElement();
const menu = new MockElement();
const toggleLabel = new MockElement();
const carousel = new MockElement();
const viewport = new MockElement();
const track = new MockElement();
const previous = new MockElement();
const next = new MockElement();
const indicators = new MockElement();
const status = new MockElement();
const documentEvents = new MockElement();
const windowEvents = new MockElement();

toggle.setAttribute('aria-expanded', 'false');
toggle.queries.set('.sr-only', toggleLabel);
menu.queryLists.set('a', []);

const slides = Array.from({length: 5}, () => {
  const slide = new MockElement();
  const link = new MockElement();
  slide.width = 400;
  slide.queries.set('a', link);
  return slide;
});
track.children = slides;

carousel.queries.set('[data-carousel-viewport]', viewport);
carousel.queries.set('[data-carousel-track]', track);
carousel.queries.set('[data-carousel-prev]', previous);
carousel.queries.set('[data-carousel-next]', next);
carousel.queries.set('[data-carousel-indicators]', indicators);
carousel.queries.set('[data-carousel-status]', status);

let visibleCount = 3;
const documentMock = {
  querySelector(selector) {
    return new Map([
      ['[data-site-header]', header],
      ['[data-nav-toggle]', toggle],
      ['[data-nav-menu]', menu],
      ['[data-carousel]', carousel]
    ]).get(selector) || null;
  },
  addEventListener: documentEvents.addEventListener.bind(documentEvents),
  createElement() {
    return new MockElement();
  }
};

const windowMock = {
  innerWidth: 1440,
  scrollY: 0,
  addEventListener: windowEvents.addEventListener.bind(windowEvents),
  requestAnimationFrame(callback) {
    callback();
  },
  getComputedStyle(element) {
    if (element === carousel) {
      return {
        getPropertyValue(name) {
          return name === '--carousel-visible' ? String(visibleCount) : '';
        }
      };
    }
    return {
      columnGap: '19.2px',
      gap: '19.2px',
      getPropertyValue() {
        return '';
      }
    };
  }
};

const script = fs.readFileSync(
  path.join(__dirname, '..', '..', 'web', 'js', 'inicio.js'),
  'utf8'
);
vm.runInNewContext(script, {
  document: documentMock,
  window: windowMock,
  console
});

assert.equal(previous.disabled, true, 'La flecha anterior inicia deshabilitada');
assert.equal(next.disabled, false, 'La flecha siguiente inicia disponible');
assert.equal(indicators.children.length, 3, 'Escritorio expone tres posiciones');
assert.equal(track.style.transform, 'translate3d(-0px, 0, 0)');
assert.equal(slides.filter((slide) => slide.getAttribute('aria-hidden') === 'false').length, 3);

next.dispatch('click');
assert.equal(track.style.transform, 'translate3d(-419.2px, 0, 0)');
assert.equal(status.textContent, 'Colecciones 2 a 4 de 5');

const endEvent = carousel.dispatch('keydown', {key: 'End'});
assert.equal(endEvent.defaultPrevented, true, 'End evita el desplazamiento de la página');
assert.equal(next.disabled, true, 'La flecha siguiente se deshabilita al final');

viewport.dispatch('pointerdown', {clientX: 100});
viewport.dispatch('pointermove', {clientX: 180});
viewport.dispatch('pointerup', {clientX: 180});
assert.equal(next.disabled, false, 'El gesto hacia la derecha regresa una posición');

visibleCount = 1;
windowEvents.dispatch('resize');
assert.equal(indicators.children.length, 5, 'Móvil expone una posición por tarjeta');
assert.equal(slides.filter((slide) => slide.getAttribute('aria-hidden') === 'false').length, 1);
assert.equal(slides.filter((slide) => slide.querySelector('a').tabIndex === 0).length, 1);

carousel.dispatch('keydown', {key: 'Home'});
const rightEvent = carousel.dispatch('keydown', {key: 'ArrowRight'});
assert.equal(rightEvent.defaultPrevented, true, 'Las flechas de teclado controlan el carrusel');
assert.equal(status.textContent, 'Colección 2 de 5');

console.log('INICIO_CAROUSEL_UI_PASS');
