-- Abendbewertungen
DELETE FROM Abendbewertungen;
DBCC CHECKIDENT ('Abendbewertungen', RESEED, 0);

-- Essensabstimmungen
DELETE FROM Essensabstimmungen;
DBCC CHECKIDENT ('Essensabstimmungen', RESEED, 0);

-- Essensrichtungen
DELETE FROM Essensrichtungen;
DBCC CHECKIDENT ('Essensrichtungen', RESEED, 0);

-- Nachrichten
DELETE FROM Nachrichten;
DBCC CHECKIDENT ('Nachrichten', RESEED, 0);

-- Spielabstimmungen
DELETE FROM Spielabstimmungen;
DBCC CHECKIDENT ('Spielabstimmungen', RESEED, 0);

-- Spieler
DELETE FROM Spieler;
DBCC CHECKIDENT ('Spieler', RESEED, 0);

-- Spielgruppen
DELETE FROM Spielgruppen;
DBCC CHECKIDENT ('Spielgruppen', RESEED, 0);

-- SpielgruppeSpieler
DELETE FROM SpielgruppeSpieler;
DBCC CHECKIDENT ('SpielgruppeSpieler', RESEED, 0);

-- Spieltermine
DELETE FROM Spieltermine;
DBCC CHECKIDENT ('Spieltermine', RESEED, 0);

-- Spielvorschlaege
DELETE FROM Spielvorschlaege;
DBCC CHECKIDENT ('Spielvorschlaege', RESEED, 0);
