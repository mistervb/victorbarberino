// Navbar scroll effect
document.addEventListener('DOMContentLoaded', () => {
    // Update current year in footer
    document.getElementById('current-year').textContent = new Date().getFullYear();

    // Throttle function to limit how often a function can be called
    function throttle(func, limit) {
        let inThrottle;
        return function() {
            const args = arguments;
            const context = this;
            if (!inThrottle) {
                func.apply(context, args);
                inThrottle = true;
                setTimeout(() => inThrottle = false, limit);
            }
        }
    }

    // Navbar scroll effect - throttled
    const navbar = document.querySelector('.navbar');
    const handleNavbarScroll = throttle(() => {
        if (window.scrollY > 50) {
            navbar.classList.add('scrolled');
        } else {
            navbar.classList.remove('scrolled');
        }
    }, 100);
    
    window.addEventListener('scroll', handleNavbarScroll);

    // Function to update active nav link based on scroll position
    const updateActiveNavLink = throttle(() => {
        const sections = document.querySelectorAll('section');
        const navLinks = document.querySelectorAll('.nav-link');
        
        // Get scroll position with respect to visible viewport
        let scrollPosition = window.scrollY + 200;

        let current = '';
        sections.forEach(section => {
            const sectionTop = section.offsetTop;
            const sectionHeight = section.clientHeight;
            if (scrollPosition >= sectionTop && scrollPosition < sectionTop + sectionHeight) {
                current = section.getAttribute('id');
            }
        });

        navLinks.forEach(link => {
            link.classList.remove('active');
            if (link.getAttribute('href') === `#${current}`) {
                link.classList.add('active');
            }
        });

        // Special case for home section
        if (window.scrollY < 100) {
            navLinks.forEach(link => link.classList.remove('active'));
            const homeLink = document.querySelector('a[href="#home"]');
            if (homeLink) homeLink.classList.add('active');
        }
    }, 200);

    // Combine scroll handlers to minimize repaints
    const handleScroll = () => {
        requestAnimationFrame(() => {
            handleNavbarScroll();
            updateActiveNavLink();
        });
    };

    window.addEventListener('scroll', handleScroll, { passive: true });

    // Smooth scroll for navigation links with improved performance
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            const targetId = this.getAttribute('href');
            const target = document.querySelector(targetId);
            if (target) {
                // Use native smooth scrolling which is more performant
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });

                // Update active class
                document.querySelectorAll('.nav-link').forEach(link => link.classList.remove('active'));
                this.classList.add('active');
                
                // Update URL without page reload for better UX
                history.pushState(null, null, targetId);
            }
        });
    });

    // Initial check for active nav link
    updateActiveNavLink();

    // Form submission
    const contactForm = document.getElementById('contact-form');
    contactForm.addEventListener('submit', (e) => {
        e.preventDefault();
        // Add form submission logic here
        alert('Mensagem enviada com sucesso!');
        contactForm.reset();
    });

    // Animation on scroll with improved performance
    const animateOnScroll = throttle(() => {
        const elements = document.querySelectorAll('.service-card:not(.animate-in), .project-card:not(.animate-in), .testimonial-card:not(.animate-in)');
        elements.forEach(element => {
            const elementTop = element.getBoundingClientRect().top;
            const windowHeight = window.innerHeight;
            if (elementTop < windowHeight - 100) {
                element.classList.add('animate-in');
            }
        });
    }, 200);

    window.addEventListener('scroll', animateOnScroll, { passive: true });
    animateOnScroll(); // Initial check
});

// Dynamic background pattern - modified to be more performance friendly
const createBackgroundPattern = () => {
    // Only create if it doesn't exist yet
    if (document.querySelector('.bg-pattern')) return;
    
    const pattern = document.createElement('div');
    pattern.className = 'bg-pattern';
    document.body.appendChild(pattern);
}

// Defer non-critical operations
if (document.readyState === 'complete') {
    createBackgroundPattern();
} else {
    window.addEventListener('load', createBackgroundPattern);
}
