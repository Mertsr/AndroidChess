// Wait for DOM to be fully loaded
document.addEventListener('DOMContentLoaded', () => {
    // Mobile menu toggle
    initMobileMenu();
    
    // Smooth scrolling for navigation links
    initSmoothScrolling();
    
    // Load personal information from API
    loadPersonalInfo();
    
    // Handle contact form submission
    handleContactForm();
    
    // Add scroll animation
    initScrollAnimations();
});

// Mobile Menu Toggle
function initMobileMenu() {
    const hamburger = document.querySelector('.hamburger');
    const navMenu = document.querySelector('.nav-menu');
    
    if (hamburger && navMenu) {
        hamburger.addEventListener('click', () => {
            navMenu.classList.toggle('active');
            hamburger.classList.toggle('active');
        });
        
        // Close menu when clicking on a link
        document.querySelectorAll('.nav-menu a').forEach(link => {
            link.addEventListener('click', () => {
                navMenu.classList.remove('active');
                hamburger.classList.remove('active');
            });
        });
    }
}

// Smooth Scrolling
function initSmoothScrolling() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                const offsetTop = target.offsetTop - 70; // Account for fixed navbar
                window.scrollTo({
                    top: offsetTop,
                    behavior: 'smooth'
                });
            }
        });
    });
}

// Load Personal Information from API
async function loadPersonalInfo() {
    try {
        const response = await fetch('/api/info');
        if (!response.ok) {
            throw new Error('Failed to fetch personal information');
        }
        
        const data = await response.json();
        
        // Update basic information
        updateBasicInfo(data);
        
        // Update social media links
        updateSocialLinks(data.socialMedia);
        
        // Update skills
        updateSkills(data.skills);
        
        // Update experience
        updateExperience(data.experience);
        
        // Update education
        updateEducation(data.education);
        
        // Update projects
        updateProjects(data.projects);
        
    } catch (error) {
        console.error('Error loading personal information:', error);
    }
}

// Update Basic Information
function updateBasicInfo(data) {
    // Navigation
    document.getElementById('nav-name').textContent = data.name;
    
    // Hero section
    document.getElementById('hero-name').textContent = data.name;
    document.getElementById('hero-title').textContent = data.title;
    document.getElementById('hero-bio').textContent = data.bio;
    
    // About section
    document.getElementById('about-bio').textContent = data.bio;
    document.getElementById('about-email').textContent = data.email;
    document.getElementById('about-phone').textContent = data.phone;
    document.getElementById('about-location').textContent = data.location;
    
    // Contact section
    document.getElementById('contact-email').textContent = data.email;
    document.getElementById('contact-phone').textContent = data.phone;
    document.getElementById('contact-location').textContent = data.location;
    
    // Footer
    document.getElementById('footer-name').textContent = data.name;
}

// Update Social Media Links
function updateSocialLinks(socialMedia) {
    if (!socialMedia) return;
    
    const linkMappings = [
        { id: 'social-linkedin', url: socialMedia.linkedin },
        { id: 'social-github', url: socialMedia.github },
        { id: 'social-twitter', url: socialMedia.twitter },
        { id: 'social-instagram', url: socialMedia.instagram },
        { id: 'footer-linkedin', url: socialMedia.linkedin },
        { id: 'footer-github', url: socialMedia.github },
        { id: 'footer-twitter', url: socialMedia.twitter },
        { id: 'footer-instagram', url: socialMedia.instagram }
    ];
    
    linkMappings.forEach(({ id, url }) => {
        const element = document.getElementById(id);
        if (element && url) {
            element.href = url;
        }
    });
}

// Update Skills
function updateSkills(skills) {
    if (!skills || skills.length === 0) return;
    
    const skillsGrid = document.getElementById('skills-grid');
    skillsGrid.innerHTML = '';
    
    skills.forEach(skill => {
        const skillItem = document.createElement('div');
        skillItem.className = 'skill-item';
        skillItem.innerHTML = `
            <i class="fas fa-code"></i>
            <h3>${skill}</h3>
        `;
        skillsGrid.appendChild(skillItem);
    });
}

// Update Experience
function updateExperience(experience) {
    if (!experience || experience.length === 0) return;
    
    const timeline = document.getElementById('experience-timeline');
    timeline.innerHTML = '';
    
    experience.forEach(exp => {
        const timelineItem = document.createElement('div');
        timelineItem.className = 'timeline-item';
        timelineItem.innerHTML = `
            <div class="timeline-content">
                <h3>${exp.title}</h3>
                <div class="company">${exp.company}</div>
                <div class="period">${exp.period}</div>
                <p>${exp.description}</p>
            </div>
        `;
        timeline.appendChild(timelineItem);
    });
}

// Update Education
function updateEducation(education) {
    if (!education || education.length === 0) return;
    
    const timeline = document.getElementById('education-timeline');
    timeline.innerHTML = '';
    
    education.forEach(edu => {
        const timelineItem = document.createElement('div');
        timelineItem.className = 'timeline-item';
        timelineItem.innerHTML = `
            <div class="timeline-content">
                <h3>${edu.degree}</h3>
                <div class="company">${edu.school}</div>
                <div class="period">${edu.period}</div>
                <p>${edu.description}</p>
            </div>
        `;
        timeline.appendChild(timelineItem);
    });
}

// Update Projects
function updateProjects(projects) {
    if (!projects || projects.length === 0) return;
    
    const projectsGrid = document.getElementById('projects-grid');
    projectsGrid.innerHTML = '';
    
    projects.forEach(project => {
        const projectCard = document.createElement('div');
        projectCard.className = 'project-card';
        projectCard.innerHTML = `
            <div class="project-content">
                <h3>${project.title}</h3>
                <p>${project.description}</p>
                ${project.link ? `<a href="${project.link}" class="project-link" target="_blank">
                    Projeyi Görüntüle <i class="fas fa-arrow-right"></i>
                </a>` : ''}
            </div>
        `;
        projectsGrid.appendChild(projectCard);
    });
}

// Handle Contact Form
function handleContactForm() {
    const contactForm = document.getElementById('contactForm');
    
    if (contactForm) {
        contactForm.addEventListener('submit', (e) => {
            e.preventDefault();
            
            const formData = {
                name: document.getElementById('name').value,
                email: document.getElementById('email').value,
                message: document.getElementById('message').value
            };
            
            // Here you can add API call to send the form data
            console.log('Form submitted:', formData);
            
            // Show success message
            alert('Mesajınız başarıyla gönderildi! Yakında size geri dönüş yapacağım.');
            
            // Reset form
            contactForm.reset();
        });
    }
}

// Scroll Animations
function initScrollAnimations() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, observerOptions);
    
    // Observe sections
    document.querySelectorAll('section').forEach(section => {
        section.style.opacity = '0';
        section.style.transform = 'translateY(20px)';
        section.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        observer.observe(section);
    });
}

// Navbar background on scroll
window.addEventListener('scroll', () => {
    const navbar = document.querySelector('.navbar');
    if (window.scrollY > 100) {
        navbar.style.boxShadow = '0 2px 10px rgba(0, 0, 0, 0.1)';
    } else {
        navbar.style.boxShadow = '0 4px 6px -1px rgba(0, 0, 0, 0.1)';
    }
});
