// Navbar scroll effect
document.addEventListener('DOMContentLoaded', () => {
    // Update current year in footer
    document.getElementById('current-year').textContent = new Date().getFullYear();

    // Navbar scroll effect
    const navbar = document.querySelector('.navbar');
    window.addEventListener('scroll', () => {
        if (window.scrollY > 50) {
            navbar.style.background = 'rgba(9, 9, 9, 0.98)';
            navbar.style.boxShadow = '0 2px 10px rgba(0, 0, 0, 0.3)';
        } else {
            navbar.style.background = 'rgba(9, 9, 9, 0.95)';
            navbar.style.boxShadow = 'none';
        }

        // Update active nav link based on scroll position
        updateActiveNavLink();
    });

    // Smooth scroll for navigation links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });

                // Update active class
                document.querySelectorAll('.nav-link').forEach(link => link.classList.remove('active'));
                this.classList.add('active');
            }
        });
    });

    // Function to update active nav link based on scroll position
    function updateActiveNavLink() {
        const sections = document.querySelectorAll('section');
        const navLinks = document.querySelectorAll('.nav-link');

        let current = '';

        sections.forEach(section => {
            const sectionTop = section.offsetTop;
            const sectionHeight = section.clientHeight;
            if (window.scrollY >= (sectionTop - sectionHeight/3)) {
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
            document.querySelector('a[href="#home"]').classList.add('active');
        }
    }

    // Initial check for active nav link
    updateActiveNavLink();

    // Projects data
    const projects = [
        {
            title: 'Sistema Financeiro',
            description: '',
            image: 'https://via.placeholder.com/400x300',
            tags: []
        },
        {
            title: 'App Mobile',
            description: 'Aplicativo móvel para iOS e Android desenvolvido com React Native.',
            image: 'https://via.placeholder.com/400x300',
            tags: ['React Native', 'JavaScript', 'API REST']
        },
        {
            title: 'Bot de Automação',
            description: 'Bot para automação de processos com interface visual interativa.',
            image: 'https://via.placeholder.com/400x300',
            tags: ['Python', 'Selenium', 'UI/UX']
        }
    ];

    // Testimonials data
    const testimonials = [
        {
            text: "Victor desenvolveu uma solução excepcional para nossa empresa. Sua expertise técnica e profissionalismo são notáveis.",
            author: "João Silva, CEO TechCorp"
        },
        {
            text: "Excelente trabalho no desenvolvimento do nosso aplicativo mobile. Superou todas as expectativas!",
            author: "Maria Santos, Startup Founder"
        },
        {
            text: "O sistema de automação desenvolvido pelo Victor trouxe resultados impressionantes para nossa operação.",
            author: "Pedro Oliveira, Diretor de Operações"
        },
         {
             text: "O sistema de automação desenvolvido pelo Victor trouxe resultados impressionantes para nossa operação.",
             author: "Pedro Oliveira, Diretor de Operações"
         }
    ];

    // Form submission
    const contactForm = document.getElementById('contact-form');
    contactForm.addEventListener('submit', (e) => {
        e.preventDefault();
        // Add form submission logic here
        alert('Mensagem enviada com sucesso!');
        contactForm.reset();
    });

    // Animation on scroll
    const animateOnScroll = () => {
        const elements = document.querySelectorAll('.service-card, .project-card, .testimonial-card');
        elements.forEach(element => {
            const elementTop = element.getBoundingClientRect().top;
            const windowHeight = window.innerHeight;
            if (elementTop < windowHeight - 100) {
                element.classList.add('animate-in');
            }
        });
    };

    window.addEventListener('scroll', animateOnScroll);
    animateOnScroll(); // Initial check
});

// Dynamic background pattern
const createBackgroundPattern = () => {
    const pattern = document.createElement('div');
    pattern.className = 'bg-pattern';
    document.body.appendChild(pattern);
}

createBackgroundPattern();
