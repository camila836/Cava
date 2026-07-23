(function () {
  'use strict';

  const header = document.querySelector('[data-site-header]');
  const toggle = document.querySelector('[data-nav-toggle]');
  const menu = document.querySelector('[data-nav-menu]');

  if (!header || !toggle || !menu) {
    return;
  }

  const toggleLabel = toggle.querySelector('.sr-only');
  let scrollFrameRequested = false;

  const setMenuState = (isOpen, returnFocus) => {
    header.classList.toggle('is-menu-open', isOpen);
    toggle.setAttribute('aria-expanded', String(isOpen));

    if (toggleLabel) {
      toggleLabel.textContent = isOpen ? 'Cerrar menú principal' : 'Abrir menú principal';
    }

    if (!isOpen && returnFocus) {
      toggle.focus();
    }
  };

  const updateHeaderState = () => {
    header.classList.toggle('is-scrolled', window.scrollY > 16);
    scrollFrameRequested = false;
  };

  toggle.addEventListener('click', () => {
    setMenuState(toggle.getAttribute('aria-expanded') !== 'true', false);
  });

  menu.querySelectorAll('a').forEach((link) => {
    link.addEventListener('click', () => setMenuState(false, false));
  });

  document.addEventListener('keydown', (event) => {
    if (event.key === 'Escape' && toggle.getAttribute('aria-expanded') === 'true') {
      setMenuState(false, true);
    }
  });

  window.addEventListener('resize', () => {
    if (window.innerWidth > 1180) {
      setMenuState(false, false);
    }
  });

  window.addEventListener('scroll', () => {
    if (!scrollFrameRequested) {
      window.requestAnimationFrame(updateHeaderState);
      scrollFrameRequested = true;
    }
  }, { passive: true });

  updateHeaderState();
}());
