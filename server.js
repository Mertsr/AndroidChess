const express = require('express');
const cors = require('cors');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(express.json());
app.use(express.static('public'));

// Personal information data (to be filled by the user)
const personalInfo = {
    name: 'Your Name',
    title: 'Your Professional Title',
    bio: 'Your biography goes here. Tell us about yourself, your journey, and what you\'re passionate about.',
    email: 'your.email@example.com',
    phone: '+90 XXX XXX XX XX',
    location: 'Your City, Turkey',
    socialMedia: {
        linkedin: 'https://linkedin.com/in/yourprofile',
        github: 'https://github.com/yourusername',
        twitter: 'https://twitter.com/yourusername',
        instagram: 'https://instagram.com/yourusername'
    },
    skills: [
        'Skill 1',
        'Skill 2',
        'Skill 3',
        'Skill 4',
        'Skill 5'
    ],
    experience: [
        {
            title: 'Job Title',
            company: 'Company Name',
            period: '2020 - Present',
            description: 'Job description and responsibilities'
        },
        {
            title: 'Previous Job Title',
            company: 'Previous Company',
            period: '2018 - 2020',
            description: 'Previous job description and responsibilities'
        }
    ],
    education: [
        {
            degree: 'Degree Name',
            school: 'University Name',
            period: '2014 - 2018',
            description: 'Major or specialization'
        }
    ],
    projects: [
        {
            title: 'Project 1',
            description: 'Project description and technologies used',
            link: 'https://project1.example.com'
        },
        {
            title: 'Project 2',
            description: 'Project description and technologies used',
            link: 'https://project2.example.com'
        }
    ]
};

// API Routes
app.get('/api/info', (req, res) => {
    res.json(personalInfo);
});

app.get('/api/info/:section', (req, res) => {
    const section = req.params.section;
    if (personalInfo[section]) {
        res.json(personalInfo[section]);
    } else {
        res.status(404).json({ error: 'Section not found' });
    }
});

// Update personal information (POST endpoint)
app.post('/api/info', (req, res) => {
    try {
        Object.assign(personalInfo, req.body);
        res.json({ success: true, message: 'Information updated successfully' });
    } catch (error) {
        res.status(500).json({ error: 'Failed to update information' });
    }
});

// Serve the main page
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

// Start server
app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});
