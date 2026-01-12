-- Cooperatives
INSERT INTO cooperatives (type, name, description, total_members, total_harvest, total_revenue, monthly_profit) 
VALUES 
('cacao', 'Coopérative de Cacao', 'Spécialisée dans la production de cacao biologique', 89, 15620.5, 5120000, 768000),
('anacarde', 'Coopérative d''Anacarde', 'Production et transformation d''anacarde', 112, 10870.0, 3850000, 577500),
('palmier', 'Coopérative de Palmier', 'Culture de palmiers à huile', 76, 8560.0, 2980000, 447000);

-- Members 
INSERT INTO members (first_name, last_name, email, phone, join_date, total_harvest, total_earnings, cooperative_id, is_active) 
VALUES 
('Emmanuel', 'Ezoua', 'emmanuel.ezoua@coopagri.ci', '+2250123456789', '2023-01-15', 45.5, 1250000, 1, true),
('Konan', 'Bella', 'konan.bella@coopagri.ci', '+2250789012345', '2023-02-20', 38.2, 980000, 1, true),
('N''dori', 'Xavier', 'ndori.xavier@coopagri.ci', '+2250567890123', '2023-03-10', 52.0, 1560000, 2, true),
('Sophie', 'Traoré', 'sophie.traore@coopagri.ci', '+2250345678901', '2023-04-05', 31.8, 850000, 3, true);
