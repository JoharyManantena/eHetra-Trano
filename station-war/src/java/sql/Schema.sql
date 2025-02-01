/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Belouh
 * Created: 21 janv. 2025
 */


drop table RelationUtilCom;
drop table ConfPrixMetreCarre;
drop table ConfCaracteristique;
drop table Maison;
drop table HistoriqueImpots;
drop table CaracteristiqueMaison;
drop table MateriauxValue;
drop table ARRONDISSEMENT;
drop table COMMUNE;


CREATE TABLE RelationUtilCom(
   id NUMBER(10),
   refuser NUMBER(10) NOT NULL,
   idCommune NUMBER(10) NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE ARRONDISSEMENT (
    ID NUMBER(10) PRIMARY KEY,
    NOM VARCHAR2(255) NOT NULL,
    ZONE SDO_GEOMETRY -- Colonne géométrique
);

CREATE TABLE COMMUNE(
   ID NUMBER(10),
   NOM VARCHAR2(255)  NOT NULL,
   ZONE SDO_GEOMETRY,
   PRIMARY KEY(id)
);

CREATE TABLE ConfPrixMetreCarre(
   id NUMBER(10),
   prix NUMBER(16,2)   NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE ConfCaracteristique(
   id NUMBER(10),
   typeParametre VARCHAR2(50)  NOT NULL,
   nomCaracteristique VARCHAR2(50)  NOT NULL,
   coefficient NUMBER(5,2)   NOT NULL,
   PRIMARY KEY(id)
);

INSERT INTO ConfCaracteristique (id, typeParametre, nomCaracteristique, coefficient) VALUES (1, 'Rindrina', 'Hazo', 0.8);
INSERT INTO ConfCaracteristique (id, typeParametre, nomCaracteristique, coefficient) VALUES (2, 'Rindrina', 'Brique', 1.1);
INSERT INTO ConfCaracteristique (id, typeParametre, nomCaracteristique, coefficient) VALUES (3, 'Rindrina', 'Beton', 1.2);
INSERT INTO ConfCaracteristique (id, typeParametre, nomCaracteristique, coefficient) VALUES (4, 'Tafo', 'Bozaka', 0.6);
INSERT INTO ConfCaracteristique (id, typeParametre, nomCaracteristique, coefficient) VALUES (5, 'Tafo', 'Tulle', 0.8);
INSERT INTO ConfCaracteristique (id, typeParametre, nomCaracteristique, coefficient) VALUES (6, 'Tafo', 'Tôle', 1.1);
INSERT INTO ConfCaracteristique (id, typeParametre, nomCaracteristique, coefficient) VALUES (7, 'Tafo', 'Beton', 1.4);

CREATE TABLE Maison(
   id NUMBER(10),
   nom VARCHAR2(255)  NOT NULL,
   longueur NUMBER(8,2)   NOT NULL,
   largeur NUMBER(8,2)   NOT NULL,
   nb_etage NUMBER(10) NOT NULL,
   log NUMBER(24,8)   NOT NULL,
   lat NUMBER(24,8)   NOT NULL,
   impots NUMBER(16,2)   NOT NULL,
   PRIMARY KEY(id)
);
ALTER TABLE Maison 
ADD (
    toit INTEGER, 
    mur INTEGER, 
    CONSTRAINT fk_maison_toit FOREIGN KEY (toit) REFERENCES ConfCaracteristique(id),
    CONSTRAINT fk_maison_mur FOREIGN KEY (mur) REFERENCES ConfCaracteristique(id)
);

CREATE TABLE HistoriqueImpots(
   id NUMBER(10),
   annees NUMBER(10) NOT NULL,
   nbMoisPayer NUMBER(10) NOT NULL,
   id_maison NUMBER(10) NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_maison) REFERENCES Maison(id)
);

CREATE TABLE CaracteristiqueMaison(
   id_confCaract NUMBER(10),
   id_maison NUMBER(10),
   PRIMARY KEY(id_confCaract, id_maison),
   FOREIGN KEY(id_confCaract) REFERENCES ConfCaracteristique(id),
   FOREIGN KEY(id_maison) REFERENCES Maison(id)
);

CREATE TABLE MateriauxValue(
   id NUMBER(10),
   mois NUMBER(10) NOT NULL,
   annees NUMBER(10) NOT NULL,
   coefficient NUMBER(15,2)   NOT NULL,
   id_caracteristique NUMBER(10) NOT NULL,
   id_commune NUMBER(10) NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_caracteristique) REFERENCES ConfCaracteristique(id),
   FOREIGN KEY(id_commune) REFERENCES Commune(id)
);

insert into MateriauxValue values(1,1,2024,1.2,1,1);
insert into MateriauxValue values(2,2,2024,1.3,1,1);
insert into MateriauxValue values(3,1,2024,1.4,2,1);
insert into MateriauxValue values(4,2,2024,1.5,2,1);
insert into MateriauxValue values(5,1,2024,1.6,3,1);
insert into MateriauxValue values(6,2,2024,1.7,3,1);
insert into MateriauxValue values(7,1,2024,1.8,4,1);
insert into MateriauxValue values(8,2,2024,1.9,4,1);
insert into MateriauxValue values(9,1,2024,1.10,5,1);
insert into MateriauxValue values(10,2,2024,1.11,5,1);
insert into MateriauxValue values(11,1,2024,1.12,6,1);
insert into MateriauxValue values(12,2,2024,1.13,6,1);
insert into MateriauxValue values(13,1,2024,1.14,7,1);
insert into MateriauxValue values(14,2,2024,1.15,7,1);


-- Trano1
INSERT INTO Maison (id, nom, longueur, largeur, nb_etage, log, lat, impots, toit, mur) 
VALUES (1, 'Trano1', 400, 200, 2, 46.984406, -18.868592, 0, 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Tôle' AND typeParametre = 'Tafo'), 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Beton' AND typeParametre = 'Rindrina'));

-- Trano2
INSERT INTO Maison (id, nom, longueur, largeur, nb_etage, log, lat, impots, toit, mur) 
VALUES (2, 'Trano2', 150, 90, 1, 47.308502, -18.807757, 0, 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Tuile' AND typeParametre = 'Tafo'), 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Brique' AND typeParametre = 'Rindrina'));

-- Trano3
INSERT INTO Maison (id, nom, longueur, largeur, nb_etage, log, lat, impots, toit, mur) 
VALUES (3, 'Trano3', 600, 700, 3, 47.223358, -18.759544, 0, 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Tuile' AND typeParametre = 'Tafo'), 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Hazo' AND typeParametre = 'Rindrina'));

-- Trano4
INSERT INTO Maison (id, nom, longueur, largeur, nb_etage, log, lat, impots, toit, mur) 
VALUES (4, 'Trano4', 300, 150, 1, 47.985535, -18.63468, 0, 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Bozaka' AND typeParametre = 'Tafo'), 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Beton' AND typeParametre = 'Rindrina'));

-- Trano5
INSERT INTO Maison (id, nom, longueur, largeur, nb_etage, log, lat, impots, toit, mur) 
VALUES (5, 'Trano5', 540, 260, 2, 48.002014, -18.755723, 0, 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Tuile' AND typeParametre = 'Tafo'), 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Brique' AND typeParametre = 'Rindrina'));

-- Trano6
INSERT INTO Maison (id, nom, longueur, largeur, nb_etage, log, lat, impots, toit, mur) 
VALUES (6, 'Trano6', 470, 350, 3, 47.960815, -18.802319, 0, 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Beton' AND typeParametre = 'Tafo'), 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Hazo' AND typeParametre = 'Rindrina'));

-- Trano7
INSERT INTO Maison (id, nom, longueur, largeur, nb_etage, log, lat, impots, toit, mur) 
VALUES (7, 'Trano7', 220, 100, 1, 47.562561, -19.176731, 0, 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Tôle' AND typeParametre = 'Tafo'), 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Brique' AND typeParametre = 'Rindrina'));

-- Trano8
INSERT INTO Maison (id, nom, longueur, largeur, nb_etage, log, lat, impots, toit, mur) 
VALUES (8, 'Trano8', 600, 210, 2, 47.643585, -19.235121, 0, 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Beton' AND typeParametre = 'Tafo'), 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Hazo' AND typeParametre = 'Rindrina'));

-- Trano9
INSERT INTO Maison (id, nom, longueur, largeur, nb_etage, log, lat, impots, toit, mur) 
VALUES (9, 'Trano9', 500, 400, 3, 47.392273, -19.180624, 0, 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Bozaka' AND typeParametre = 'Tafo'), 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Beton' AND typeParametre = 'Rindrina'));

-- Trano10
INSERT INTO Maison (id, nom, longueur, largeur, nb_etage, log, lat, impots, toit, mur) 
VALUES (10, 'Trano10', 250, 300, 4, 47.60376, -18.491392, 0, 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Tuile' AND typeParametre = 'Tafo'), 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Beton' AND typeParametre = 'Rindrina'));

-- Trano11
INSERT INTO Maison (id, nom, longueur, largeur, nb_etage, log, lat, impots, toit, mur) 
VALUES (11, 'Trano11', 260, 100, 3, 47.584534, -18.535692, 0, 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Tuile' AND typeParametre = 'Tafo'), 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Hazo' AND typeParametre = 'Rindrina'));

-- Trano12
INSERT INTO Maison (id, nom, longueur, largeur, nb_etage, log, lat, impots, toit, mur) 
VALUES (12, 'Trano12', 255.5, 200, 2, 47.727356, -18.521361, 0, 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Tôle' AND typeParametre = 'Tafo'), 
        (SELECT id FROM ConfCaracteristique WHERE nomCaracteristique = 'Brique' AND typeParametre = 'Rindrina'));








/*-----------------------------------Configuration--------------------------------------*/



INSERT INTO USER_SDO_GEOM_METADATA (
    TABLE_NAME, COLUMN_NAME, DIMINFO, SRID
) VALUES (
    'ARRONDISSEMENT', 
    'ZONE', 
    SDO_DIM_ARRAY(
        SDO_DIM_ELEMENT('X', 47.0, 48.0, 0.005), -- Limites pour longitude
        SDO_DIM_ELEMENT('Y', -19.0, -18.0, 0.005) -- Limites pour latitude
    ),
    4326 -- Définition du SRID
);
COMMIT;

INSERT INTO USER_SDO_GEOM_METADATA (
    TABLE_NAME, COLUMN_NAME, DIMINFO, SRID
) VALUES (
    'COMMUNE', 
    'ZONE', 
    SDO_DIM_ARRAY(
        SDO_DIM_ELEMENT('X', 47.0, 48.0, 0.005), -- Limites pour longitude
        SDO_DIM_ELEMENT('Y', -19.0, -18.0, 0.005) -- Limites pour latitude
    ),
    4326 -- Définition du SRID
);
COMMIT;


CREATE INDEX IDX_COMMUNE_ZONE 
ON COMMUNE(ZONE)
INDEXTYPE IS MDSYS.SPATIAL_INDEX;

CREATE INDEX IDX_ARRONDISSEMENT_ZONE 
ON ARRONDISSEMENT(ZONE)
INDEXTYPE IS MDSYS.SPATIAL_INDEX;


SELECT * 
FROM USER_SDO_GEOM_METADATA 
WHERE TABLE_NAME = 'ARRONDISSEMENT' AND COLUMN_NAME = 'ZONE';

SELECT * 
FROM USER_SDO_GEOM_METADATA 
WHERE TABLE_NAME = 'COMMUNE' AND COLUMN_NAME = 'ZONE';



SELECT ID, NOM, SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(ZONE, 0.005) AS VALIDATION
FROM ARRONDISSEMENT;

/**************Donnee*****************/
INSERT INTO COMMUNE (ID, NOM, ZONE)
VALUES (
    1,
    'Commune ANTANANARIVO',
    SDO_GEOMETRY(2003, 4326, NULL,
        SDO_ELEM_INFO_ARRAY(1, 1003, 1),
        SDO_ORDINATE_ARRAY(
            46.66992188, -18.77891637,
            46.91162109, -18.53690856,
            47.22473145, -18.47960906,
            47.50488281, -18.21369822, 
            47.85644531, -18.26065336,
            48.41674805, -18.59418886,
            48.23547363, -18.92707243,
            47.97180176, -19.19705344,
            47.76306152, -19.41479244,
            47.36755371, -19.461413,
            47.12585449, -19.2644798,
            46.76879883, -19.04654131,
            46.66992188, -18.77891637
        )
    )
);

INSERT INTO COMMUNE (ID, NOM, ZONE)
VALUES (
    2,
    'Commune ANTSIRABE',
    SDO_GEOMETRY(2003, 4326, NULL,
        SDO_ELEM_INFO_ARRAY(1, 1003, 1),
        SDO_ORDINATE_ARRAY(
            46.70837402, -19.4717713,
            47.52685547, -19.53390722,
            47.4005127, -19.83389278, 
            47.14782715, -20.05593127,
            46.73583984, -19.95785975, 
            46.42272949, -19.58049348, 
            46.70837402, -19.4717713  
        )
    )
);




INSERT INTO ARRONDISSEMENT (ID, NOM, ZONE)
VALUES (
    1,
    '1 ere arrondissement',
    SDO_GEOMETRY(2003, 4326, NULL,
        SDO_ELEM_INFO_ARRAY(1, 1003, 1),
        SDO_ORDINATE_ARRAY(
            47.080536, -18.589103,
            46.796265, -18.840271, 
            46.918488, -18.99235,
            47.176666, -19.028725, 
            47.558441, -18.848073,
            47.513123, -18.643793, 
            47.194519, -18.563054,
            47.080536, -18.589103 
        )
    )
);		
			


INSERT INTO ARRONDISSEMENT (ID, NOM, ZONE)
VALUES (
    2,
    '2 eme arrondissement',
    SDO_GEOMETRY(2003, 4326, NULL,
        SDO_ELEM_INFO_ARRAY(1, 1003, 1),
        SDO_ORDINATE_ARRAY(
            47.794647,  -18.714083,	
            47.794647,	-18.776539,	
            47.849579,	-18.842872,	
            47.900391,	-18.89228,	
            48.010254,	-18.958568,	
            48.132477,	-18.776539,	
            48.253326,	-18.655511,	
            48.212128,	-18.539607,	
            48.063812,	-18.482278,	
            47.897644,	-18.466639,	
            47.922363,	-18.496612,	
            47.794647,	-18.714083
        )
    )
);
	

INSERT INTO ARRONDISSEMENT (ID, NOM, ZONE)
VALUES (
    3,
    '3 eme arrondissement',
    SDO_GEOMETRY(2003, 4326, NULL,
        SDO_ELEM_INFO_ARRAY(1, 1003, 1),
        SDO_ORDINATE_ARRAY(
            47.327728,	-19.07807,	
            47.617493,	-19.075472,	
            47.765808,	-19.272739,	
            47.436218,	-19.357025,	
            47.264557,	-19.266254,	
            47.327728,	-19.07807
        )
    )
);

	


	

INSERT INTO ARRONDISSEMENT (ID, NOM, ZONE)
VALUES (
    4,
    '4 eme arrondissement',
    SDO_GEOMETRY(2003, 4326, NULL,
        SDO_ELEM_INFO_ARRAY(1, 1003, 1),
        SDO_ORDINATE_ARRAY(
            47.463684,	-18.377986,	
            47.590027,	-18.691951,	
            47.82486,	-18.552627,	
            47.627106,	-18.329732,	
            47.463684,	-18.377986
        )
    )
);

	




