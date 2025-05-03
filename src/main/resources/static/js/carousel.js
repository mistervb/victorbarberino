/**
 * Otimização do carrossel de depoimentos
 * Script separado para melhor organização e performance
 */
document.addEventListener('DOMContentLoaded', () => {
  const container = document.querySelector('.testimonials-container');
  const prevBtn = document.querySelector('.prev-btn');
  const nextBtn = document.querySelector('.next-btn');
  
  if (container && prevBtn && nextBtn) {
    // Initialize carousel
    container.scrollLeft = 0;
    
    // Calcula o tamanho real do card + gap
    const card = container.querySelector('.testimonial-card');
    const gap = parseInt(getComputedStyle(container).gap) || 16;
    const cardWidth = card ? card.offsetWidth + gap : container.offsetWidth;
    const autoPlayInterval = 7000;
    let autoPlay;
    
    // Função para iniciar autoplay com requestAnimationFrame
    const startAutoPlay = () => {
      autoPlay = setTimeout(() => {
        // Usando requestAnimationFrame para animações mais suaves
        requestAnimationFrame(() => {
          container.scrollBy({ left: cardWidth, behavior: 'smooth' });
        });
        startAutoPlay();
      }, autoPlayInterval);
    };
    
    // Iniciar autoplay
    startAutoPlay();

    // Função otimizada para manipular cliques nos botões
    const handleButtonClick = (direction) => {
      clearTimeout(autoPlay);
      
      requestAnimationFrame(() => {
        container.scrollBy({ 
          left: direction * cardWidth, 
          behavior: 'smooth' 
        });
      });
      
      startAutoPlay();
    };

    // Event listeners otimizados
    prevBtn.addEventListener('click', () => handleButtonClick(-1));
    nextBtn.addEventListener('click', () => handleButtonClick(1));

    // Pause/resume autoplay on hover and touch - com throttling
    container.addEventListener('mouseenter', () => clearTimeout(autoPlay));
    container.addEventListener('mouseleave', startAutoPlay);
    container.addEventListener('touchstart', () => clearTimeout(autoPlay), { passive: true });
    container.addEventListener('touchend', startAutoPlay);

    // Suporte a arraste otimizado com pointer events
    let isDown = false, startX = 0, scrollStart = 0;
    
    // Usa passive: true para melhor performance em eventos de toque
    container.addEventListener('pointerdown', e => {
      isDown = true;
      container.setPointerCapture(e.pointerId);
      startX = e.clientX - container.getBoundingClientRect().left;
      scrollStart = container.scrollLeft;
      clearTimeout(autoPlay);
      container.classList.add('active');
    }, { passive: true });
    
    // Otimiza o movimento usando requestAnimationFrame
    let ticking = false;
    container.addEventListener('pointermove', e => {
      if (!isDown) return;
      
      // Evita múltiplas chamadas com requestAnimationFrame
      if (!ticking) {
        requestAnimationFrame(() => {
          const x = e.clientX - container.getBoundingClientRect().left;
          const walk = x - startX;
          container.scrollLeft = scrollStart - walk;
          ticking = false;
        });
        ticking = true;
      }
    }, { passive: true });
    
    const endDrag = e => {
      if (!isDown) return;
      isDown = false;
      container.releasePointerCapture(e.pointerId);
      container.classList.remove('active');
      startAutoPlay();
    };
    
    container.addEventListener('pointerup', endDrag, { passive: true });
    container.addEventListener('pointerleave', endDrag, { passive: true });
    container.addEventListener('pointercancel', endDrag, { passive: true });
  }
});
