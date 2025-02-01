-- Vérifiez si Oracle Spatial est activé et installez-le si nécessaire.

DROP SEQUENCE seq_arrondissement;

CREATE SEQUENCE seq_arrondissement START WITH 1 INCREMENT BY 1;

-- Table Arrondissement (représenté par un polygone)
CREATE TABLE Arrondissement (
    ID_Arrondissement INT PRIMARY KEY,
    Nom_Arrondissement VARCHAR(100) NOT NULL,
    Zone SDO_GEOMETRY NOT NULL -- Représente la zone de l'arrondissement sous forme de polygone
);

-- Table Maison mise à jour avec une relation vers l'arrondissement
CREATE TABLE Maison (
    ID_Maison INT PRIMARY KEY,
    Nom_Maison VARCHAR(100) NOT NULL,
    Surface FLOAT NOT NULL,
    Localisation SDO_GEOMETRY NOT NULL, -- Coordonnées géographiques de la maison (point)
    Nombre_Etages INT NOT NULL,
    ID_Arrondissement INT, -- Relation vers l'arrondissement
    CONSTRAINT fk_maison_arrondissement FOREIGN KEY (ID_Arrondissement) REFERENCES Arrondissement(ID_Arrondissement)
);



-- Delete ancien metadonne 

DELETE FROM USER_SDO_GEOM_METADATA
WHERE TABLE_NAME = 'ARRONDISSEMENT' AND COLUMN_NAME = 'ZONE';

DELETE FROM USER_SDO_GEOM_METADATA
WHERE TABLE_NAME = 'MAISON' AND COLUMN_NAME = 'LOCALISATION';




-- Pour ARRONDISSEMENT
INSERT INTO USER_SDO_GEOM_METADATA (
    TABLE_NAME, COLUMN_NAME, DIMINFO, SRID
) VALUES (
    'ARRONDISSEMENT',
    'ZONE',
    SDO_DIM_ARRAY(
        SDO_DIM_ELEMENT('X', 0, 100000, 0.005),
        SDO_DIM_ELEMENT('Y', 0, 100000, 0.005)
    ),
    NULL
);

-- Pour MAISON
INSERT INTO USER_SDO_GEOM_METADATA (
    TABLE_NAME, COLUMN_NAME, DIMINFO, SRID
) VALUES (
    'MAISON',
    'LOCALISATION',
    SDO_DIM_ARRAY(
        SDO_DIM_ELEMENT('X', 0, 100000, 0.005),
        SDO_DIM_ELEMENT('Y', 0, 100000, 0.005)
    ),
    NULL
);


-- Si il existe index 

DROP INDEX idx_arrondissement_zone;
DROP INDEX idx_maison_localisation;



-- REQUIRED KEYS
-- Index spatial pour ARRONDISSEMENT
CREATE INDEX idx_arrondissement_zone 
ON Arrondissement(Zone) 
INDEXTYPE IS MDSYS.SPATIAL_INDEX;

-- Index spatial pour MAISON
CREATE INDEX idx_maison_localisation 
ON Maison(Localisation) 
INDEXTYPE IS MDSYS.SPATIAL_INDEX;



-- verification
SELECT ID_Arrondissement, SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(Zone, 0.005)
FROM Arrondissement;

SELECT ID_Maison, SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(Localisation, 0.005)
FROM Maison;




-- DONNEE DE TEST WITH ALL THE FUNCT VALUES BELOW -- --------------------------------

-- Insérez un polygone valide dans ARRONDISSEMENT
INSERT INTO Arrondissement (ID_Arrondissement, Nom_Arrondissement, Zone)
VALUES (
    1,
    'Arrondissement A',
    SDO_GEOMETRY(
        2003, NULL, NULL,
        SDO_ELEM_INFO_ARRAY(1, 1003, 1),
        SDO_ORDINATE_ARRAY(0, 0, 0, 10, 10, 10, 10, 0, 0, 0)
    )
); -- 2003 : Type Polygon



-- Insérez un point valide dans MAISON
INSERT INTO Maison (ID_Maison, Nom_Maison, Surface, Localisation, Nombre_Etages)
VALUES (
    1,
    'Maison 1',
    120.5,
    SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(5, 5, NULL), NULL, NULL),
    2
);


SELECT M.Nom_Maison, M.Surface, A.Nom_Arrondissement
FROM Maison M
JOIN Arrondissement A
ON SDO_RELATE(M.Localisation, A.Zone, 'MASK=INSIDE') = 'TRUE';



-- Donnee de test 

-- Insertion des données de test pour les maisons
INSERT INTO Maison (ID_Maison, Nom_Maison, Surface, Localisation, Nombre_Etages)
VALUES (1, 'Maison 1', 100.0, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(10, 10, NULL), NULL, NULL), 2);

INSERT INTO Maison (ID_Maison, Nom_Maison, Surface, Localisation, Nombre_Etages)
VALUES (2, 'Maison 2', 120.0, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(15, 15, NULL), NULL, NULL), 1);

INSERT INTO Maison (ID_Maison, Nom_Maison, Surface, Localisation, Nombre_Etages)
VALUES (3, 'Maison 3', 90.0, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(20, 20, NULL), NULL, NULL), 1);

INSERT INTO Maison (ID_Maison, Nom_Maison, Surface, Localisation, Nombre_Etages)
VALUES (4, 'Maison 4', 110.0, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(25, 25, NULL), NULL, NULL), 2);

INSERT INTO Maison (ID_Maison, Nom_Maison, Surface, Localisation, Nombre_Etages)
VALUES (5, 'Maison 5', 95.0, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(30, 30, NULL), NULL, NULL), 3);

INSERT INTO Maison (ID_Maison, Nom_Maison, Surface, Localisation, Nombre_Etages)
VALUES (6, 'Maison 6', 130.0, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(35, 35, NULL), NULL, NULL), 1);

INSERT INTO Maison (ID_Maison, Nom_Maison, Surface, Localisation, Nombre_Etages)
VALUES (7, 'Maison 7', 105.0, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(40, 40, NULL), NULL, NULL), 2);

INSERT INTO Maison (ID_Maison, Nom_Maison, Surface, Localisation, Nombre_Etages)
VALUES (8, 'Maison 8', 115.0, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(45, 45, NULL), NULL, NULL), 3);

INSERT INTO Maison (ID_Maison, Nom_Maison, Surface, Localisation, Nombre_Etages)
VALUES (9, 'Maison 9', 100.0, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(50, 50, NULL), NULL, NULL), 1);

INSERT INTO Maison (ID_Maison, Nom_Maison, Surface, Localisation, Nombre_Etages)
VALUES (10, 'Maison 10', 125.0, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(55, 55, NULL), NULL, NULL), 2);




-- Insertion de deux arrondissements avec des polygones initiaux
INSERT INTO Arrondissement (ID_Arrondissement, Nom_Arrondissement, Zone)
VALUES (
    1,
    'Arrondissement 1',
    SDO_GEOMETRY(
        2003, NULL, NULL,
        SDO_ELEM_INFO_ARRAY(1, 1003, 1),
        SDO_ORDINATE_ARRAY(5, 5, 5, 50, 50, 50, 50, 5, 5, 5) -- X Y {5} ############ (10, 10) -----> (20, 20) -----> (30, 10)

        -- SDO_ORDINATE_ARRAY(0, 0, 0, 60, 60, 60, 60, 0, 0, 0)
    )
);

INSERT INTO Arrondissement (ID_Arrondissement, Nom_Arrondissement, Zone)
VALUES (
    2,
    'Arrondissement 2',
    SDO_GEOMETRY(
        2003, NULL, NULL,
        SDO_ELEM_INFO_ARRAY(1, 1003, 1),
        SDO_ORDINATE_ARRAY(30, 30, 30, 60, 60, 60, 60, 30, 30, 30)
        -- SDO_ORDINATE_ARRAY(20, 20, 20, 70, 70, 70, 70, 20, 20, 20)
    )
);




-- requete recherche par arrondissement 

WITH Maison_Arrondissement AS (
    SELECT M.Nom_Maison, M.Surface, A.Nom_Arrondissement, 
           ROW_NUMBER() OVER (PARTITION BY M.ID_Maison ORDER BY A.ID_Arrondissement ASC) AS rn
    FROM Maison M
    JOIN Arrondissement A
    ON SDO_RELATE(M.Localisation, A.Zone, 'MASK=INSIDE') = 'TRUE'
)
SELECT Nom_Maison, Surface, Nom_Arrondissement
FROM Maison_Arrondissement
WHERE rn = 2;



-- requete count arrondissement 
WITH Maison_Arrondissement AS (
    SELECT M.Nom_Maison, M.Surface, A.Nom_Arrondissement,
           ROW_NUMBER() OVER (PARTITION BY M.ID_Maison ORDER BY A.ID_Arrondissement ASC) AS rn
    FROM Maison M
    JOIN Arrondissement A
    ON SDO_RELATE(M.Localisation, A.Zone, 'MASK=INSIDE') = 'TRUE'
)
SELECT COUNT(*) AS Nombre_Maison
FROM Maison_Arrondissement
WHERE rn = 2; -- identity arrondissement
