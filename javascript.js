// ===== DONNÉES DE L'APPLICATION =====
const appData = {
    currentSection: 'dashboard',
    currentCooperative: 'cacao',
    membersCount: 147,
    harvestData: {
        cacao: 12450,
        anacarde: 8720,
        palmier: 5340
    },
    revenue: 4250000,
    members: [
        {
            id: 1,
            name: "Emmanuel Ezoua",
            phone: "+225 01 23 45 67 89",
            email: "emmanuel.ezoua@coopagri.ci",
            cooperatives: ['cacao', 'anacarde'],
            joinDate: '2023-01-15',
            harvests: 45,
            revenue: 1250000
        },
        {
            id: 2,
            name: "Konan Bella",
            phone: "+225 07 89 12 34 56",
            email: "konan.bella@coopagri.ci",
            cooperatives: ['cacao'],
            joinDate: '2023-02-20',
            harvests: 38,
            revenue: 980000
        },
        {
            id: 3,
            name: "N'dori Xavier",
            phone: "+225 05 67 89 12 34",
            email: "ndori.xavier@coopagri.ci",
            cooperatives: ['anacarde', 'palmier'],
            joinDate: '2023-03-10',
            harvests: 52,
            revenue: 1560000
        },
        {
            id: 4,
            name: "Sophie Traoré",
            phone: "+225 03 45 67 89 12",
            email: "sophie.traore@coopagri.ci",
            cooperatives: ['palmier'],
            joinDate: '2023-04-05',
            harvests: 31,
            revenue: 850000
        }
    ],
    harvests: [
        {
            id: 1,
            date: '2025-01-15',
            cooperative: 'cacao',
            product: 'Cacao',
            quantity: '250 kg',
            member: 'Emmanuel Ezoua',
            quality: 'Excellente'
        },
        {
            id: 2,
            date: '2025-01-14',
            cooperative: 'anacarde',
            product: 'Anacarde',
            quantity: '180 kg',
            member: 'N\'dori Xavier',
            quality: 'Bonne'
        },
        {
            id: 3,
            date: '2025-01-13',
            cooperative: 'palmier',
            product: 'Huile de palme',
            quantity: '120 L',
            member: 'Sophie Traoré',
            quality: 'Excellente'
        },
        {
            id: 4,
            date: '2025-01-12',
            cooperative: 'cacao',
            product: 'Cacao',
            quantity: '310 kg',
            member: 'Konan Bella',
            quality: 'Moyenne'
        }
    ],
    activities: [
        {
            id: 1,
            icon: 'seedling',
            title: 'Nouvelle récolte enregistrée',
            description: 'Emmanuel Ezoua a enregistré 250 kg de cacao de qualité excellente',
            time: 'Il y a 2 heures',
            type: 'harvest',
            cooperative: 'cacao'
        },
        {
            id: 2,
            icon: 'user-plus',
            title: 'Nouveau membre ajouté',
            description: 'Marie Akissi a rejoint la coopérative de cacao',
            time: 'Il y a 1 jour',
            type: 'member',
            cooperative: 'cacao'
        },
        {
            id: 3,
            icon: 'money-bill-wave',
            title: 'Paiement distribué',
            description: 'Distribution des bénéfices du trimestre aux 147 membres',
            time: 'Il y a 3 jours',
            type: 'payment',
            cooperative: 'all'
        },
        {
            id: 4,
            icon: 'shipping-fast',
            title: 'Livraison effectuée',
            description: '2 tonnes de cacao expédiées vers l\'acheteur principal',
            time: 'Il y a 5 jours',
            type: 'shipment',
            cooperative: 'cacao'
        },
        {
            id: 5,
            icon: 'chart-line',
            title: 'Rapport mensuel généré',
            description: 'Rapport de performance du mois de janvier disponible',
            time: 'Il y a 1 semaine',
            type: 'report',
            cooperative: 'all'
        }
    ],
    charts: {
        harvestChart: null,
        profitChart: null,
        profitEvolutionChart: null,
        coopDistributionChart: null
    }
};

// ===== INITIALISATION =====
document.addEventListener('DOMContentLoaded', function () {
    initApp();
    setupEventListeners();
});

function initApp() {
    updateCurrentDate();
    updateStats();
    loadActivities();
    loadMembers();
    loadHarvests();
    setupCharts();
    setupModals();

    // Animation initiale
    setTimeout(() => {
        showNotification('Bienvenue !', 'CoopAgri Pro est prêt à l\'emploi', 'success');
    }, 1000);
}

// ===== GESTION DE L'INTERFACE =====
function switchSection(sectionId) {
    // Mettre à jour la navigation
    document.querySelectorAll('.main-nav a').forEach(link => {
        link.classList.remove('active');
    });
    document.querySelector(`.main-nav a[onclick*="${sectionId}"]`).classList.add('active');

    // Mettre à jour les sections
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    });
    document.getElementById(`${sectionId}-section`).classList.add('active');

    // Fermer le menu mobile
    document.getElementById('main-nav').classList.remove('active');

    // Mettre à jour l'état
    appData.currentSection = sectionId;

    // Notification de changement de section
    const sectionNames = {
        'dashboard': 'Tableau de bord',
        'members': 'Membres',
        'harvests': 'Récoltes',
        'profits': 'Bénéfices',
        'settings': 'Paramètres'
    };

    showNotification('Navigation', `Section ${sectionNames[sectionId]} chargée`, 'info');

    // Redessiner les graphiques si nécessaire
    if (sectionId === 'dashboard' || sectionId === 'profits') {
        setTimeout(() => {
            if (sectionId === 'dashboard') {
                if (appData.charts.harvestChart) appData.charts.harvestChart.resize();
                if (appData.charts.profitChart) appData.charts.profitChart.resize();
            } else if (sectionId === 'profits') {
                if (appData.charts.profitEvolutionChart) appData.charts.profitEvolutionChart.resize();
                if (appData.charts.coopDistributionChart) appData.charts.coopDistributionChart.resize();
            }
        }, 100);
    }
}

// ===== GESTION DES COOPÉRATIVES =====
function selectCooperative(coopType) {
    appData.currentCooperative = coopType;

    // Mettre à jour les boutons
    document.querySelectorAll('.coop-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelector(`.coop-btn.${coopType}`).classList.add('active');

    // Mettre à jour les statistiques
    updateStats();

    // Filtrer les activités
    filterActivitiesByCooperative(coopType);

    showNotification('Coopérative', `Coopérative ${getCoopName(coopType)} sélectionnée`, 'info');
}

function getCoopName(coopType) {
    const names = {
        'cacao': 'de Cacao',
        'anacarde': 'd\'Anacarde',
        'palmier': 'de Palmier'
    };
    return names[coopType] || '';
}

// ===== STATISTIQUES =====
function updateStats() {
    const coop = appData.currentCooperative;
    const statsData = {
        'cacao': { cacao: '15,620', anacarde: '2,340', palmier: '1,150', members: '89', revenue: '5.12M' },
        'anacarde': { cacao: '3,450', anacarde: '10,870', palmier: '2,210', members: '112', revenue: '3.85M' },
        'palmier': { cacao: '1,980', anacarde: '3,210', palmier: '8,560', members: '76', revenue: '2.98M' }
    };

    const data = statsData[coop];

    animateValue('cacao-harvest', data.cacao);
    animateValue('anacarde-harvest', data.anacarde);
    animateValue('palmier-harvest', data.palmier);
    animateValue('members-count', data.members);
    animateValue('revenue-amount', data.revenue + ' FCFA');
}

function animateValue(elementId, finalValue) {
    const element = document.getElementById(elementId);
    element.textContent = finalValue;

    // Animation
    element.style.transform = 'scale(1.2)';
    setTimeout(() => {
        element.style.transition = 'transform 0.3s ease';
        element.style.transform = 'scale(1)';
    }, 300);
}

// ===== ACTIVITÉS =====
function loadActivities() {
    const container = document.getElementById('activity-list');
    container.innerHTML = '';

    appData.activities.forEach(activity => {
        const activityItem = document.createElement('div');
        activityItem.className = 'activity-item';
        activityItem.innerHTML = `
            <div class="activity-icon">
                <i class="fas fa-${activity.icon}"></i>
            </div>
            <div class="activity-content">
                <h4>${activity.title}</h4>
                <p>${activity.description}</p>
            </div>
            <div class="activity-time">${activity.time}</div>
        `;
        container.appendChild(activityItem);
    });
}

function addActivity(activity) {
    activity.id = Date.now();
    appData.activities.unshift(activity);

    if (appData.activities.length > 10) {
        appData.activities.pop();
    }

    loadActivities();
}

function filterActivities(searchTerm) {
    const items = document.querySelectorAll('.activity-item');
    const term = searchTerm.toLowerCase();

    items.forEach(item => {
        const text = item.textContent.toLowerCase();
        item.style.display = text.includes(term) ? 'flex' : 'none';
    });
}

function filterActivitiesByCooperative(coop) {
    const items = document.querySelectorAll('.activity-item');

    if (coop === 'all') {
        items.forEach(item => item.style.display = 'flex');
    } else {
        // Pour la démo, on affiche tout
        items.forEach(item => item.style.display = 'flex');
    }
}

// ===== MEMBRES =====
function loadMembers() {
    const container = document.getElementById('members-grid');
    container.innerHTML = '';

    appData.members.forEach(member => {
        const memberCard = document.createElement('div');
        memberCard.className = 'member-card';

        const coopTags = member.cooperatives.map(coop =>
            `<span class="coop-tag ${coop}">${getCoopName(coop)}</span>`
        ).join('');

        memberCard.innerHTML = `
            <div class="member-header">
                <div class="member-avatar">${member.name.split(' ').map(n => n[0]).join('')}</div>
                <div class="member-info">
                    <h4>${member.name}</h4>
                    <p><i class="fas fa-phone"></i> ${member.phone}</p>
                    <p><i class="fas fa-envelope"></i> ${member.email}</p>
                </div>
            </div>
            <div class="member-coops">
                ${coopTags}
            </div>
            <div class="member-stats">
                <div class="member-stat">
                    <div class="value">${member.harvests}</div>
                    <div class="label">Récoltes</div>
                </div>
                <div class="member-stat">
                    <div class="value">${(member.revenue / 1000000).toFixed(2)}M</div>
                    <div class="label">Revenus</div>
                </div>
            </div>
        `;

        container.appendChild(memberCard);
    });
}

// ===== RÉCOLTES =====
function loadHarvests() {
    const tbody = document.querySelector('#harvest-table tbody');
    tbody.innerHTML = '';

    appData.harvests.forEach(harvest => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${formatDate(harvest.date)}</td>
            <td><span class="harvest-coop ${harvest.cooperative}">${getCoopName(harvest.cooperative)}</span></td>
            <td>${harvest.product}</td>
            <td><strong>${harvest.quantity}</strong></td>
            <td>${harvest.member}</td>
            <td><span class="${getQualityClass(harvest.quality)}">${harvest.quality}</span></td>
        `;
        tbody.appendChild(row);
    });
}

function getQualityClass(quality) {
    const classes = {
        'Excellente': 'badge success',
        'Bonne': 'badge warning',
        'Moyenne': 'badge warning',
        'Mauvaise': 'badge danger'
    };
    return classes[quality] || '';
}

// ===== GRAPHIQUES =====
function setupCharts() {
    // Détruire les anciens graphiques s'ils existent
    if (appData.charts.harvestChart) appData.charts.harvestChart.destroy();
    if (appData.charts.profitChart) appData.charts.profitChart.destroy();
    if (appData.charts.profitEvolutionChart) appData.charts.profitEvolutionChart.destroy();
    if (appData.charts.coopDistributionChart) appData.charts.coopDistributionChart.destroy();

    // Attendre que le DOM soit prêt
    setTimeout(() => {
        // Graphique des récoltes
        const harvestCanvas = document.getElementById('harvestChart');
        if (harvestCanvas) {
            const harvestCtx = harvestCanvas.getContext('2d');
            appData.charts.harvestChart = new Chart(harvestCtx, {
                type: 'bar',
                data: {
                    labels: ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin'],
                    datasets: [{
                        label: 'Cacao (kg)',
                        data: [1200, 1900, 3000, 5000, 2000, 3000],
                        backgroundColor: 'rgba(139, 69, 19, 0.7)',
                        borderColor: 'rgba(139, 69, 19, 1)',
                        borderWidth: 1
                    }, {
                        label: 'Anacarde (kg)',
                        data: [800, 1200, 1500, 2000, 1800, 2200],
                        backgroundColor: 'rgba(255, 152, 0, 0.7)',
                        borderColor: 'rgba(255, 152, 0, 1)',
                        borderWidth: 1
                    }, {
                        label: 'Palmier (L)',
                        data: [500, 800, 1200, 1500, 1000, 1400],
                        backgroundColor: 'rgba(56, 142, 60, 0.7)',
                        borderColor: 'rgba(56, 142, 60, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'top',
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        }

        // Graphique des bénéfices (camembert)
        const profitCanvas = document.getElementById('profitChart');
        if (profitCanvas) {
            const profitCtx = profitCanvas.getContext('2d');
            appData.charts.profitChart = new Chart(profitCtx, {
                type: 'doughnut',
                data: {
                    labels: ['Cacao', 'Anacarde', 'Palmier', 'Autres'],
                    datasets: [{
                        data: [45, 30, 20, 5],
                        backgroundColor: [
                            'rgba(139, 69, 19, 0.8)',
                            'rgba(255, 152, 0, 0.8)',
                            'rgba(56, 142, 60, 0.8)',
                            'rgba(128, 128, 128, 0.8)'
                        ],
                        borderWidth: 2
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'right',
                        }
                    }
                }
            });
        }

        // Graphique d'évolution des bénéfices
        const profitEvolutionCanvas = document.getElementById('profitEvolutionChart');
        if (profitEvolutionCanvas) {
            const profitEvolutionCtx = profitEvolutionCanvas.getContext('2d');
            appData.charts.profitEvolutionChart = new Chart(profitEvolutionCtx, {
                type: 'line',
                data: {
                    labels: ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin'],
                    datasets: [{
                        label: 'Bénéfices (millions FCFA)',
                        data: [3.2, 3.8, 4.5, 5.2, 6.1, 7.2],
                        borderColor: 'rgba(46, 125, 50, 1)',
                        backgroundColor: 'rgba(46, 125, 50, 0.1)',
                        borderWidth: 3,
                        fill: true,
                        tension: 0.4
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'top',
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        }

        // Graphique de distribution
        const coopDistributionCanvas = document.getElementById('coopDistributionChart');
        if (coopDistributionCanvas) {
            const coopDistributionCtx = coopDistributionCanvas.getContext('2d');
            appData.charts.coopDistributionChart = new Chart(coopDistributionCtx, {
                type: 'pie',
                data: {
                    labels: ['Cacao', 'Anacarde', 'Palmier'],
                    datasets: [{
                        data: [58, 30, 12],
                        backgroundColor: [
                            'rgba(139, 69, 19, 0.8)',
                            'rgba(255, 152, 0, 0.8)',
                            'rgba(56, 142, 60, 0.8)'
                        ],
                        borderWidth: 2
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'right',
                        }
                    }
                }
            });
        }
    }, 500);
}

// ===== MODALES =====
function setupModals() {
    // Gestion des fermetures
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') {
            closeAllModals();
        }
    });

    // Menu mobile
    document.getElementById('mobile-menu-btn').addEventListener('click', function () {
        document.getElementById('main-nav').classList.toggle('active');
    });

    // Menu utilisateur
    document.getElementById('user-menu-btn').addEventListener('click', function () {
        showNotification('Profil', 'Menu utilisateur ouvert', 'info');
    });
}

function openModal(modalId) {
    document.getElementById(modalId).classList.add('active');
    document.body.style.overflow = 'hidden';

    // Pré-remplir les dates
    const today = new Date().toISOString().split('T')[0];
    document.querySelectorAll('input[type="date"]').forEach(input => {
        if (!input.value) input.value = today;
    });

    document.querySelectorAll('input[type="datetime-local"]').forEach(input => {
        if (!input.value) {
            const now = new Date();
            now.setHours(now.getHours() + 1);
            input.value = now.toISOString().slice(0, 16);
        }
    });
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.remove('active');
    document.body.style.overflow = '';

    // Réinitialiser les formulaires
    const form = document.querySelector(`#${modalId} form`);
    if (form) form.reset();
}

function closeAllModals() {
    document.querySelectorAll('.modal.active').forEach(modal => {
        modal.classList.remove('active');
    });
    document.body.style.overflow = '';
}

// ===== FONCTIONS DES MODALES =====
function selectCoopOption(element, coopType) {
    const container = element.parentElement;
    container.querySelectorAll('.coop-option').forEach(opt => {
        opt.classList.remove('selected');
    });
    element.classList.add('selected');
    // Ajouter l'attribut data-coop
    element.setAttribute('data-coop', coopType);
}

function selectFormat(element, format) {
    const container = element.parentElement;
    container.querySelectorAll('.coop-option').forEach(opt => {
        opt.classList.remove('selected');
    });
    element.classList.add('selected');
}

function saveHarvest() {
    const coop = document.querySelector('#harvest-modal .coop-option.selected').getAttribute('data-coop');
    const quantity = document.getElementById('harvest-quantity').value;
    const member = document.getElementById('harvest-member').value;
    const quality = document.getElementById('harvest-quality').value;

    if (!quantity || quantity <= 0) {
        showNotification('Erreur', 'Veuillez entrer une quantité valide', 'error');
        return;
    }

    // Ajouter la récolte
    const newHarvest = {
        id: appData.harvests.length + 1,
        date: document.getElementById('harvest-date').value || new Date().toISOString().split('T')[0],
        cooperative: coop,
        product: coop === 'cacao' ? 'Cacao' : coop === 'anacarde' ? 'Anacarde' : 'Huile de palme',
        quantity: `${quantity} ${coop === 'palmier' ? 'L' : 'kg'}`,
        member: member,
        quality: quality
    };

    appData.harvests.unshift(newHarvest);
    loadHarvests();

    // Ajouter une activité
    addActivity({
        icon: 'seedling',
        title: 'Nouvelle récolte enregistrée',
        description: `${member} a enregistré ${quantity} ${coop === 'palmier' ? 'L' : 'kg'} de ${coop === 'cacao' ? 'cacao' : coop === 'anacarde' ? 'anacarde' : 'huile de palme'}`,
        time: 'À l\'instant',
        type: 'harvest',
        cooperative: coop
    });

    // Mettre à jour les statistiques
    appData.harvestData[coop] += parseFloat(quantity);
    updateStats();

    // Mettre à jour le graphique des récoltes
    if (appData.charts.harvestChart) {
        // Simuler une mise à jour des données
        const datasetIndex = coop === 'cacao' ? 0 : coop === 'anacarde' ? 1 : 2;
        const currentData = appData.charts.harvestChart.data.datasets[datasetIndex].data;
        const lastValue = currentData[currentData.length - 1];
        currentData[currentData.length - 1] = lastValue + parseFloat(quantity) / 100;
        appData.charts.harvestChart.update();
    }

    showNotification('Récolte', 'Récolte enregistrée avec succès', 'success');
    closeModal('harvest-modal');
}

function saveMember() {
    const name = document.getElementById('member-name').value;
    const phone = document.getElementById('member-phone').value;
    const email = document.getElementById('member-email').value;

    if (!name || !phone) {
        showNotification('Erreur', 'Veuillez remplir tous les champs obligatoires', 'error');
        return;
    }

    // Récupérer les coopératives sélectionnées
    const selectedCoops = [];
    document.querySelectorAll('#member-modal .coop-option.selected').forEach(coop => {
        selectedCoops.push(coop.getAttribute('data-coop'));
    });

    // Ajouter le membre
    const newMember = {
        id: appData.members.length + 1,
        name: name,
        phone: phone,
        email: email || 'non-spécifié',
        cooperatives: selectedCoops.length > 0 ? selectedCoops : ['cacao'],
        joinDate: document.getElementById('member-join-date').value || new Date().toISOString().split('T')[0],
        harvests: Math.floor(Math.random() * 50) + 10,
        revenue: Math.floor(Math.random() * 2000000) + 500000
    };

    appData.members.push(newMember);
    loadMembers();

    // Mettre à jour le compteur
    appData.membersCount++;
    document.getElementById('members-count').textContent = appData.membersCount;

    // Ajouter une activité
    addActivity({
        icon: 'user-plus',
        title: 'Nouveau membre ajouté',
        description: `${name} a rejoint la coopérative`,
        time: 'À l\'instant',
        type: 'member',
        cooperative: selectedCoops[0] || 'cacao'
    });

    showNotification('Membre', `${name} ajouté avec succès`, 'success');
    closeModal('member-modal');
}

function savePayment() {
    const member = document.getElementById('payment-member').value;
    const amount = document.getElementById('payment-amount').value;
    const type = document.getElementById('payment-type').value;

    if (!member || !amount || amount <= 0) {
        showNotification('Erreur', 'Veuillez remplir tous les champs obligatoires', 'error');
        return;
    }

    const typeNames = {
        'cotisation': 'Cotisation',
        'achat': 'Achat de produits',
        'benefice': 'Distribution de bénéfices',
        'autre': 'Paiement'
    };

    addActivity({
        icon: 'hand-holding-usd',
        title: 'Paiement enregistré',
        description: `${typeNames[type]} de ${parseInt(amount).toLocaleString()} FCFA pour ${member}`,
        time: 'À l\'instant',
        type: 'payment',
        cooperative: 'all'
    });

    showNotification('Paiement', `Paiement de ${amount} FCFA enregistré`, 'success');
    closeModal('payment-modal');
}

function generateReport() {
    const type = document.getElementById('report-type').value;
    const coop = document.getElementById('report-coop').value;

    const typeNames = {
        'monthly': 'mensuel',
        'quarterly': 'trimestriel',
        'annual': 'annuel',
        'harvest': 'de récoltes',
        'financial': 'financier'
    };

    const coopNames = {
        'all': 'toutes les coopératives',
        'cacao': 'de cacao',
        'anacarde': 'd\'anacarde',
        'palmier': 'de palmier'
    };

    addActivity({
        icon: 'chart-bar',
        title: 'Rapport généré',
        description: `Rapport ${typeNames[type]} pour la coopérative ${coopNames[coop]}`,
        time: 'À l\'instant',
        type: 'report',
        cooperative: 'all'
    });

    showNotification('Rapport', `Rapport ${typeNames[type]} généré avec succès`, 'success');
    closeModal('report-modal');
}

function saveMeeting() {
    const title = document.getElementById('meeting-title').value;
    const date = document.getElementById('meeting-date').value;
    const location = document.getElementById('meeting-location').value;

    if (!title || !date || !location) {
        showNotification('Erreur', 'Veuillez remplir tous les champs obligatoires', 'error');
        return;
    }

    addActivity({
        icon: 'calendar-check',
        title: 'Réunion planifiée',
        description: `${title} prévue le ${formatDateTime(date)} à ${location}`,
        time: 'À l\'instant',
        type: 'meeting',
        cooperative: 'all'
    });

    showNotification('Réunion', `"${title}" planifiée avec succès`, 'success');
    closeModal('meeting-modal');
}

function distributeProfits() {
    const total = document.getElementById('profit-total').value;
    const coop = document.getElementById('profit-coop').value;

    if (!total || total <= 0) {
        showNotification('Erreur', 'Veuillez entrer un montant valide', 'error');
        return;
    }

    const coopNames = {
        'all': 'toutes les coopératives',
        'cacao': 'de cacao',
        'anacarde': 'd\'anacarde',
        'palmier': 'de palmier'
    };

    addActivity({
        icon: 'share-alt',
        title: 'Bénéfices distribués',
        description: `Distribution de ${parseInt(total).toLocaleString()} FCFA aux membres de la coopérative ${coopNames[coop]}`,
        time: 'À l\'instant',
        type: 'profit',
        cooperative: 'all'
    });

    // Mettre à jour le graphique des bénéfices
    if (appData.charts.profitChart && coop !== 'all') {
        const coopIndex = coop === 'cacao' ? 0 : coop === 'anacarde' ? 1 : 2;
        const currentValue = appData.charts.profitChart.data.datasets[0].data[coopIndex];
        appData.charts.profitChart.data.datasets[0].data[coopIndex] = currentValue + 5;
        appData.charts.profitChart.update();
    }

    showNotification('Distribution', `Bénéfices distribués avec succès`, 'success');
    closeModal('profit-modal');
}

// ===== NOTIFICATIONS =====
function showNotification(title, message, type = 'success') {
    const notification = document.getElementById('notification');
    const notificationTitle = document.getElementById('notification-title');
    const notificationText = document.getElementById('notification-text');

    // Mettre à jour l'icône
    const icon = notification.querySelector('i');
    icon.className = `fas fa-${type === 'success' ? 'check-circle' :
        type === 'error' ? 'exclamation-circle' :
            type === 'warning' ? 'exclamation-triangle' : 'info-circle'}`;

    notificationTitle.textContent = title;
    notificationText.textContent = message;
    notification.className = `notification ${type} active`;

    // Fermer automatiquement
    setTimeout(closeNotification, 5000);
}

function closeNotification() {
    document.getElementById('notification').classList.remove('active');
}

// ===== UTILITAIRES =====
function updateCurrentDate() {
    const now = new Date();
    const options = {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    };
    document.getElementById('current-date').textContent =
        now.toLocaleDateString('fr-FR', options);
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('fr-FR');
}

function formatDateTime(datetimeStr) {
    const date = new Date(datetimeStr);
    return date.toLocaleString('fr-FR');
}

function setupEventListeners() {
    // Rafraîchissement automatique des données
    setInterval(() => {
        // Simuler des mises à jour
        if (Math.random() > 0.7) {
            appData.harvestData.cacao += Math.floor(Math.random() * 100);
            updateStats();
        }
    }, 30000);

    // Raccourcis clavier
    document.addEventListener('keydown', function (e) {
        // Ctrl + S pour sauvegarder
        if (e.ctrlKey && e.key === 's') {
            e.preventDefault();
            showNotification('Sauvegarde', 'Toutes les données ont été sauvegardées', 'success');
        }
        // Ctrl + H pour aide
        if (e.ctrlKey && e.key === 'h') {
            e.preventDefault();
            showNotification('Aide', 'Menu d\'aide ouvert', 'info');
        }
    });

    // Gestion du scroll pour le menu
    window.addEventListener('scroll', function () {
        const header = document.querySelector('header');
        if (window.scrollY > 50) {
            header.style.boxShadow = '0 8px 20px rgba(0,0,0,0.2)';
        } else {
            header.style.boxShadow = '0 4px 12px rgba(0,0,0,0.15)';
        }
    });

    // Redimensionner les graphiques quand la fenêtre change de taille
    window.addEventListener('resize', function () {
        if (appData.charts.harvestChart) appData.charts.harvestChart.resize();
        if (appData.charts.profitChart) appData.charts.profitChart.resize();
        if (appData.charts.profitEvolutionChart) appData.charts.profitEvolutionChart.resize();
        if (appData.charts.coopDistributionChart) appData.charts.coopDistributionChart.resize();
    });
}