(() => {
  'use strict';

  const app = document.getElementById('unidades-app');
  if (!app) return;

  const endpoint = app.dataset.endpoint;
  const csrfToken = document.querySelector('meta[name="csrf-token"]')?.content || '';
  const form = document.getElementById('unidad-form');
  const idInput = document.getElementById('unidad-id');
  const descriptionInput = document.getElementById('unidad-descripcion');
  const descriptionError = document.getElementById('descripcion-error');
  const formTitle = document.getElementById('form-title');
  const saveButton = document.getElementById('save-button');
  const cancelEdit = document.getElementById('cancel-edit');
  const reloadButton = document.getElementById('reload-button');
  const globalMessage = document.getElementById('global-message');
  const loadingState = document.getElementById('loading-state');
  const emptyState = document.getElementById('empty-state');
  const tableWrap = document.getElementById('table-wrap');
  const tableBody = document.getElementById('unidades-body');
  const deleteDialog = document.getElementById('delete-dialog');
  const deleteName = document.getElementById('delete-name');
  const confirmDelete = document.getElementById('confirm-delete');
  const units = new Map();
  let pendingDeleteId = null;

  const setBusy = (button, busy) => {
    button.disabled = busy;
    button.setAttribute('aria-busy', String(busy));
  };

  const showMessage = (message, error = false) => {
    globalMessage.textContent = message;
    globalMessage.classList.toggle('notice-error', error);
    globalMessage.hidden = false;
  };

  const clearMessage = () => {
    globalMessage.textContent = '';
    globalMessage.hidden = true;
  };

  const request = async (method, payload) => {
    const headers = { Accept: 'application/json' };
    const options = { method, headers, credentials: 'same-origin' };
    let target = endpoint;
    if (method === 'DELETE') {
      headers['X-CSRF-Token'] = csrfToken;
      target = `${endpoint}?id=${encodeURIComponent(payload.id)}`;
    } else if (method !== 'GET') {
      headers['Content-Type'] = 'application/json;charset=UTF-8';
      headers['X-CSRF-Token'] = csrfToken;
      options.body = JSON.stringify(payload);
    }
    let response;
    try {
      response = await fetch(target, options);
    } catch (networkError) {
      throw new Error('No fue posible conectar con el servidor.');
    }
    const contentType = response.headers.get('content-type') || '';
    if (response.redirected && response.url.includes('/login')) {
      window.location.assign(response.url);
      throw new Error('La sesión terminó.');
    }
    if (!contentType.includes('application/json')) {
      throw new Error('El servidor devolvió una respuesta inesperada.');
    }
    let body;
    try {
      body = await response.json();
    } catch (parseError) {
      throw new Error('El servidor devolvió JSON inválido.');
    }
    if (!response.ok || !body.ok) {
      const error = new Error(body.message || 'No fue posible completar la operación.');
      error.errors = body.errors || {};
      throw error;
    }
    return body;
  };

  const actionButton = (label, action, id, className) => {
    const button = document.createElement('button');
    button.type = 'button';
    button.className = `table-action ${className}`;
    button.dataset.action = action;
    button.dataset.id = String(id);
    button.textContent = label;
    return button;
  };

  const render = () => {
    tableBody.replaceChildren();
    const ordered = Array.from(units.values()).sort((left, right) =>
      left.descripcion.localeCompare(right.descripcion, 'es', { sensitivity: 'base' }));
    for (const unit of ordered) {
      const row = document.createElement('tr');
      const descriptionCell = document.createElement('td');
      descriptionCell.textContent = unit.descripcion;
      const actionsCell = document.createElement('td');
      actionsCell.className = 'table-actions';
      actionsCell.append(
        actionButton('Editar', 'edit', unit.id, 'table-action-edit'),
        actionButton('Eliminar', 'delete', unit.id, 'table-action-delete')
      );
      row.append(descriptionCell, actionsCell);
      tableBody.append(row);
    }
    loadingState.hidden = true;
    emptyState.hidden = ordered.length !== 0;
    tableWrap.hidden = ordered.length === 0;
  };

  const loadUnits = async () => {
    clearMessage();
    loadingState.hidden = false;
    emptyState.hidden = true;
    tableWrap.hidden = true;
    setBusy(reloadButton, true);
    try {
      const body = await request('GET');
      units.clear();
      for (const unit of body.data) units.set(unit.id, unit);
      render();
    } catch (error) {
      loadingState.hidden = true;
      showMessage(error.message, true);
    } finally {
      setBusy(reloadButton, false);
    }
  };

  const resetForm = () => {
    form.reset();
    idInput.value = '';
    descriptionError.textContent = '';
    formTitle.textContent = 'Nueva unidad';
    saveButton.textContent = 'Guardar';
    cancelEdit.hidden = true;
  };

  const startEdit = (unit) => {
    idInput.value = String(unit.id);
    descriptionInput.value = unit.descripcion;
    formTitle.textContent = 'Editar unidad';
    saveButton.textContent = 'Guardar cambios';
    cancelEdit.hidden = false;
    descriptionInput.focus();
  };

  form.addEventListener('submit', async (event) => {
    event.preventDefault();
    clearMessage();
    descriptionError.textContent = '';
    const description = descriptionInput.value.trim();
    if (!description || description.length > 45) {
      descriptionError.textContent = !description
        ? 'La descripción es obligatoria.'
        : 'La descripción admite máximo 45 caracteres.';
      descriptionInput.focus();
      return;
    }
    const id = Number(idInput.value);
    const editing = Number.isInteger(id) && id > 0;
    setBusy(saveButton, true);
    try {
      const body = await request(editing ? 'PUT' : 'POST',
        editing ? { id, descripcion: description } : { descripcion: description });
      units.set(body.data.id, body.data);
      render();
      resetForm();
      showMessage(body.message);
    } catch (error) {
      descriptionError.textContent = error.errors?.descripcion || '';
      showMessage(error.message, true);
    } finally {
      setBusy(saveButton, false);
    }
  });

  tableBody.addEventListener('click', (event) => {
    const button = event.target.closest('button[data-action]');
    if (!button) return;
    const id = Number(button.dataset.id);
    const unit = units.get(id);
    if (!unit) return;
    if (button.dataset.action === 'edit') {
      startEdit(unit);
      return;
    }
    pendingDeleteId = id;
    deleteName.textContent = unit.descripcion;
    deleteDialog.showModal();
  });

  confirmDelete.addEventListener('click', async () => {
    if (!Number.isInteger(pendingDeleteId)) return;
    const id = pendingDeleteId;
    setBusy(confirmDelete, true);
    try {
      const body = await request('DELETE', { id });
      units.delete(id);
      if (Number(idInput.value) === id) resetForm();
      render();
      deleteDialog.close();
      showMessage(body.message);
    } catch (error) {
      deleteDialog.close();
      showMessage(error.message, true);
    } finally {
      pendingDeleteId = null;
      setBusy(confirmDelete, false);
    }
  });

  deleteDialog.addEventListener('close', () => {
    pendingDeleteId = null;
  });
  cancelEdit.addEventListener('click', resetForm);
  reloadButton.addEventListener('click', loadUnits);
  loadUnits();
})();
