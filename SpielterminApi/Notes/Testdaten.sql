-- Spieler
INSERT INTO Spieler(Benutzername, PasswordHash, Vorname, Nachname, Wohnort, PLZ, Straﬂe, Hausnummer)
VALUES ('Benutzer1', 'PasswordHash1', 'Max', 'Mustermann', 'Stadt1', '12345', 'Straﬂe1', '1A'),
       ('Benutzer2', 'PasswordHash2', 'Anna', 'Musterfrau', 'Stadt2', '67890', 'Straﬂe2', '2B');

-- Spielgruppe
INSERT INTO Spielgruppen(Name)
VALUES ('Gruppe1'),
       ('Gruppe2');

-- Essensrichtung
INSERT INTO Essensrichtungen(Art)
VALUES ('Italienisch'),
       ('Griechisch');

-- Spieltermin
INSERT INTO Spieltermine(Termin, SpielgruppeId, GastgeberId)
VALUES ('2023-10-13 16:45:00', 1, 1),
       ('2023-10-23 16:45:00', 2, 2);

-- Spielvorschlag
INSERT INTO Spielvorschlaege(SpielvorschlagName, SpielterminId, SpielerId)
VALUES ('Spiel1', 1, 1),
       ('Spiel2', 2, 2);

-- SpielgruppeSpieler
INSERT INTO SpielgruppeSpieler(SpielgruppeId, SpielerId, WarGastgeber)
VALUES (1, 1, 1),
       (2, 2, 0);

-- Nachricht
INSERT INTO Nachrichten(NachrichtText, Uhrzeit, AbsenderId, SpielgruppeId)
VALUES ('Nachricht1', '2023-10-13 16:45:00', 1, 1),
       ('Nachricht2', '2023-10-23 16:45:00', 2, 2);

-- Spielabstimmung
INSERT INTO Spielabstimmungen(Zustimmung, SpielgruppeId, SpielvorschlagId,SpielerId)
VALUES ('1', 1, 1,1),
       ('0', 2, 2,2);

-- Abendbewertung
INSERT INTO Abendbewertungen(AbendbewertungName, Essensbewertung, Gastgeberbewertung, SpielerId, SpielterminId)
VALUES (6, 7, 6, 1, 1),
       (5, 5, 3, 2, 2);

-- Essensabstimmung
INSERT INTO Essensabstimmungen([EssensrichtungId], SpielerId, SpielterminId)
VALUES (1, 1, 1),
       (2, 2, 2);
