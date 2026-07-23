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

  const carousel = document.querySelector('[data-carousel]');
  if (!carousel) {
    return;
  }

  const viewport = carousel.querySelector('[data-carousel-viewport]');
  const track = carousel.querySelector('[data-carousel-track]');
  const previousButton = carousel.querySelector('[data-carousel-prev]');
  const nextButton = carousel.querySelector('[data-carousel-next]');
  const indicators = carousel.querySelector('[data-carousel-indicators]');
  const status = carousel.querySelector('[data-carousel-status]');
  const slides = track ? Array.from(track.children) : [];

  if (!viewport || !track || !previousButton || !nextButton || !indicators || !slides.length) {
    return;
  }

  let currentIndex = 0;
  let maximumIndex = 0;
  let resizeFrameRequested = false;
  let pointerStartX = null;
  let pointerMoved = false;

  const getVisibleCount = () => {
    const value = Number.parseInt(
      window.getComputedStyle(carousel).getPropertyValue('--carousel-visible'),
      10
    );
    return Number.isFinite(value) && value > 0 ? value : 1;
  };

  const rebuildIndicators = () => {
    const expected = maximumIndex + 1;

    if (indicators.children.length === expected) {
      return;
    }

    indicators.replaceChildren();

    for (let position = 0; position < expected; position += 1) {
      const indicator = document.createElement('button');
      indicator.type = 'button';
      indicator.className = 'carousel-indicator';
      indicator.setAttribute('aria-label', `Ir a la posición ${position + 1} de ${expected}`);
      indicator.addEventListener('click', () => {
        currentIndex = position;
        updateCarousel(true);
      });
      indicators.appendChild(indicator);
    }
  };

  const updateCarousel = (announce) => {
    const visibleCount = getVisibleCount();
    maximumIndex = Math.max(0, slides.length - visibleCount);
    currentIndex = Math.min(Math.max(currentIndex, 0), maximumIndex);

    const slideWidth = slides[0].getBoundingClientRect().width;
    const trackStyles = window.getComputedStyle(track);
    const gap = Number.parseFloat(trackStyles.columnGap || trackStyles.gap) || 0;
    track.style.transform = `translate3d(-${currentIndex * (slideWidth + gap)}px, 0, 0)`;

    previousButton.disabled = currentIndex === 0;
    nextButton.disabled = currentIndex === maximumIndex;

    rebuildIndicators();
    Array.from(indicators.children).forEach((indicator, position) => {
      if (position === currentIndex) {
        indicator.setAttribute('aria-current', 'true');
      } else {
        indicator.removeAttribute('aria-current');
      }
    });

    slides.forEach((slide, position) => {
      const isVisible = position >= currentIndex && position < currentIndex + visibleCount;
      const link = slide.querySelector('a');
      slide.setAttribute('aria-hidden', String(!isVisible));
      if (link) {
        link.tabIndex = isVisible ? 0 : -1;
      }
    });

    if (announce && status) {
      const firstVisible = currentIndex + 1;
      const lastVisible = Math.min(slides.length, currentIndex + visibleCount);
      status.textContent = visibleCount === 1
        ? `Colección ${firstVisible} de ${slides.length}`
        : `Colecciones ${firstVisible} a ${lastVisible} de ${slides.length}`;
    }
  };

  const moveCarousel = (direction) => {
    const nextIndex = Math.min(Math.max(currentIndex + direction, 0), maximumIndex);
    if (nextIndex !== currentIndex) {
      currentIndex = nextIndex;
      updateCarousel(true);
    }
  };

  previousButton.addEventListener('click', () => moveCarousel(-1));
  nextButton.addEventListener('click', () => moveCarousel(1));

  carousel.addEventListener('keydown', (event) => {
    if (event.key === 'ArrowLeft') {
      event.preventDefault();
      moveCarousel(-1);
    } else if (event.key === 'ArrowRight') {
      event.preventDefault();
      moveCarousel(1);
    } else if (event.key === 'Home') {
      event.preventDefault();
      currentIndex = 0;
      updateCarousel(true);
    } else if (event.key === 'End') {
      event.preventDefault();
      currentIndex = maximumIndex;
      updateCarousel(true);
    }
  });

  viewport.addEventListener('pointerdown', (event) => {
    pointerStartX = event.clientX;
    pointerMoved = false;
  });

  viewport.addEventListener('pointermove', (event) => {
    if (pointerStartX !== null && Math.abs(event.clientX - pointerStartX) > 12) {
      pointerMoved = true;
    }
  });

  viewport.addEventListener('pointerup', (event) => {
    if (pointerStartX === null) {
      return;
    }

    const distance = event.clientX - pointerStartX;
    pointerStartX = null;

    if (Math.abs(distance) >= 44) {
      moveCarousel(distance > 0 ? -1 : 1);
    }
  });

  viewport.addEventListener('pointercancel', () => {
    pointerStartX = null;
    pointerMoved = false;
  });

  viewport.addEventListener('click', (event) => {
    if (pointerMoved) {
      event.preventDefault();
      pointerMoved = false;
    }
  }, true);

  window.addEventListener('resize', () => {
    if (!resizeFrameRequested) {
      window.requestAnimationFrame(() => {
        updateCarousel(false);
        resizeFrameRequested = false;
      });
      resizeFrameRequested = true;
    }
  });

  updateCarousel(false);
}());
