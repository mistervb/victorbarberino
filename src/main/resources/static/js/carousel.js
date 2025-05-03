/**
 * Carrossel otimizado para depoimentos e projetos
 * Implementa navegação por botões, autoplay e interação touch
 */
document.addEventListener('DOMContentLoaded', () => {
  // Configurações para os diferentes carrosséis
  const carousels = [
    {
      name: 'testimonials',
      containerSelector: '.testimonials-container',
      cardSelector: '.testimonial-card',
      carouselSelector: '.testimonial-carousel',
      autoPlayInterval: 7000
    },
    {
      name: 'projects',
      containerSelector: '.projects-container',
      cardSelector: '.project-card',
      carouselSelector: '.project-carousel',
      autoPlayInterval: 8000
    }
  ];
  
  // Inicializa cada carrossel
  carousels.forEach(config => {
    initCarousel(config);
  });
  
  function initCarousel(config) {
    const container = document.querySelector(config.containerSelector);
    const carousel = document.querySelector(config.carouselSelector);
    
    if (!container || !carousel) return;
    
    const prevBtn = carousel.querySelector('.prev-btn');
    const nextBtn = carousel.querySelector('.next-btn');
    const cards = container.querySelectorAll(config.cardSelector);
    
    if (!cards.length) return;
    
    // Reset scroll position
    container.scrollLeft = 0;
    
    // Calculate card width with gap
    const gap = parseInt(getComputedStyle(container).gap) || 16;
    const cardWidth = cards[0].offsetWidth + gap;
    
    // Autoplay functionality
    let autoPlayTimer = null;
    
    const startAutoPlay = () => {
      stopAutoPlay();
      autoPlayTimer = setTimeout(() => {
        container.scrollBy({
          left: cardWidth,
          behavior: 'smooth'
        });
        
        // If we reached the end, scroll back to start
        const maxScroll = container.scrollWidth - container.clientWidth;
        if (container.scrollLeft >= maxScroll - 10) {
          setTimeout(() => {
            container.scrollTo({
              left: 0,
              behavior: 'smooth'
            });
          }, 500);
        }
        
        startAutoPlay();
      }, config.autoPlayInterval);
    };
    
    const stopAutoPlay = () => {
      if (autoPlayTimer) {
        clearTimeout(autoPlayTimer);
        autoPlayTimer = null;
      }
    };
    
    // Button navigation
    if (prevBtn && nextBtn) {
      prevBtn.addEventListener('click', () => {
        stopAutoPlay();
        container.scrollBy({
          left: -cardWidth,
          behavior: 'smooth'
        });
        startAutoPlay();
      });
      
      nextBtn.addEventListener('click', () => {
        stopAutoPlay();
        container.scrollBy({
          left: cardWidth,
          behavior: 'smooth'
        });
        startAutoPlay();
      });
    }
    
    // Touch/mouse drag functionality
    let isDragging = false;
    let startPosition = 0;
    let startScrollPosition = 0;
    
    // Mouse events
    container.addEventListener('mousedown', (e) => {
      isDragging = true;
      container.classList.add('active');
      startPosition = e.pageX - container.offsetLeft;
      startScrollPosition = container.scrollLeft;
      stopAutoPlay();
    });
    
    container.addEventListener('mousemove', (e) => {
      if (!isDragging) return;
      e.preventDefault();
      const x = e.pageX - container.offsetLeft;
      const walk = (x - startPosition) * 2;
      container.scrollLeft = startScrollPosition - walk;
    });
    
    window.addEventListener('mouseup', () => {
      if (!isDragging) return;
      isDragging = false;
      container.classList.remove('active');
      startAutoPlay();
    });
    
    // Touch events
    container.addEventListener('touchstart', (e) => {
      isDragging = true;
      container.classList.add('active');
      startPosition = e.touches[0].pageX - container.offsetLeft;
      startScrollPosition = container.scrollLeft;
      stopAutoPlay();
    }, { passive: true });
    
    container.addEventListener('touchmove', (e) => {
      if (!isDragging) return;
      const x = e.touches[0].pageX - container.offsetLeft;
      const walk = (x - startPosition) * 2;
      container.scrollLeft = startScrollPosition - walk;
    }, { passive: true });
    
    container.addEventListener('touchend', () => {
      isDragging = false;
      container.classList.remove('active');
      startAutoPlay();
    }, { passive: true });
    
    // Pause autoplay on hover
    container.addEventListener('mouseenter', stopAutoPlay);
    container.addEventListener('mouseleave', startAutoPlay);
    
    // Start autoplay
    startAutoPlay();
  }
});
