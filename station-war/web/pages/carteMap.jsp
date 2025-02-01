<%@page import="models.RelationUtilCom"%>
<%@page import="user.UserEJB"%>
<%@page import="historique.MapUtilisateur"%>
<%@page import="models.Arrondissement"%>
<%@page import="models.Maison"%>
<%@page import="models.ConfCaracteristique"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="java.util.Vector"%>

<%
    UserEJB ue = (UserEJB) session.getValue("u");
    MapUtilisateur utilisateur = ue.getUser();
    int id_commune = 0; 
    RelationUtilCom rel = RelationUtilCom.getCommune(utilisateur.getRefuser());
    id_commune = rel.getIdcommune();
    Vector<ConfCaracteristique> listeMur = ConfCaracteristique.getConf("Mur");
    Vector<ConfCaracteristique> listeToit = ConfCaracteristique.getConf("Toit");
    Vector<Maison> listeMaison = Maison.getAll();
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Voire la carte</title>
    <link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css" />
    <script src="https://unpkg.com/leaflet/dist/leaflet.js"></script>
    <script src="https://unpkg.com/leaflet.wkt/leaflet.wkt.js"></script>

    <style>
        #map {
            height: 100vh; /* La carte occupe 100% de la hauteur de la fenêtre */
            width: 100%; /* La carte occupe toute la largeur de la fenêtre */
        }
        
        /* Personnalisation des boutons de zoom */
        .leaflet-control-zoom {
            position: absolute !important;
            right: 10px; /* Décalage depuis la droite */
            top: 100px; /* Décalage depuis le haut */
        }
        
        /* Style pour la boîte modale */
        .modal {
            display: none;
            position: absolute;
            width: 300px;
            background: white;
            border: 1px solid #ccc;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            padding: 20px;
            z-index: 1000;
            height: 58vh;
            overflow-y: auto;
        }

        .petitModal{
            height: 31vh !important;
        }
        
        .modal-header {
            font-weight: bold;
            margin-bottom: 10px;
        }

        .modal input, .modal select {
            width: 100%;
            padding: 5px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .modal button {
            padding: 10px 20px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .modal button:hover {
            background-color: #218838;
        }

        #data-table {
            margin: 20px auto;
            width: 80%;
            border-collapse: collapse;
        }

        #data-table th, #data-table td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: center;
        }

        #data-table th {
            background-color: #f4f4f4;
        }
        
        #formModal3 {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%); /* Centre la modale par rapport à son propre centre */
            width: 500px; /* Largeur de la modale */
            background-color: white;
            height: 50%;
            border: 1px solid #ccc;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            padding: 20px;
            z-index: 1000;
            display: none; /* La modale est masquée par défaut */
            overflow-y: auto;
        }
        
        /* Style de la table */
        #impotsTable {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        #impotsTable th, #impotsTable td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: center;
        }

        #impotsTable th {
            background-color: #f4f4f4;
            font-weight: bold;
        }

        #impotsTable tbody tr:nth-child(odd) {
            background-color: #f9f9f9;
        }

        #impotsTable tbody tr:nth-child(even) {
            background-color: #fff;
        }

        #impotsTable tbody tr:hover {
            background-color: #f1f1f1;
        }
        
        .container-Arrondissement {
            font-family: Arial, sans-serif;
            background-color: #f7f9fc;
            margin: 70px auto;
            padding: 40px;
            display: flex;
            flex-direction: column;
            align-items: center;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            max-width: 900px;
            width: 95%;
        }

        .title-Arrondissement {
            color: #000000;
            text-align: center;
            margin-bottom: 20px;
            font-size: 24px;
            font-weight: bold;
        }

        .form-Arrondissement {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
        }

        .form-Arrondissement input[type="number"],
        .form-Arrondissement select{
            padding: 10px;
            font-size: 16px;
            border: 1px solid #bdc3c7;
            border-radius: 5px;
            width: 200px;
        }
        
        select#moisFacture {
            padding: 3px 40px 3px 20px;
        }

        .form-Arrondissement button {
            padding: 10px 20px;
            font-size: 16px;
            color: #fff;
            background-color: #3498db;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .form-Arrondissement button:hover {
            background-color: #2980b9;
        }



        /* Style de base pour la table */
            .table-Arrondissement {
                width: 100%;
                border-collapse: collapse;
                margin: 25px 0;
                font-size: 0.9em;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                min-width: 600px;
                box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
                animation: fadeIn 1s ease-in-out;
            }

            /* Animation d'apparition de la table */
            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: translateY(20px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            /* Style de l'en-tête de la table */
            .table-Arrondissement thead tr {
                background-color: #2c3e50; /* Couleur sombre pour l'en-tête */
                color: #ffffff;
                text-align: left;
            }

            /* Style des cellules de l'en-tête */
            .table-Arrondissement th {
                padding: 12px 15px;
                font-weight: bold;
                text-transform: uppercase;
                letter-spacing: 0.1em;
            }

            /* Style des cellules du corps de la table */
            .table-Arrondissement td {
                padding: 12px 15px;
                border-bottom: 1px solid #dddddd;
                transition: background-color 0.3s ease, transform 0.3s ease;
            }

            /* Style des lignes impaires */
            .table-Arrondissement tbody tr:nth-of-type(odd) {
                background-color: #f9f9f9;
            }

            /* Style des lignes au survol */
            .table-Arrondissement tbody tr:hover {
                background-color: #f1f1f1;
                transform: scale(1.02);
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }

            /* Style pour les colonnes de sommes */
            .table-Arrondissement tbody tr td:nth-child(4),
            .table-Arrondissement tbody tr td:nth-child(5) {
                font-weight: bold;
                color: #e74c3c; /* Rouge pour les impôts non payés */
            }

            .table-Arrondissement tbody tr td:nth-child(4) {
                color: #27ae60; /* Vert pour les impôts payés */
            }

            /* Animation pour l'apparition des lignes */
            @keyframes slideIn {
                from {
                    opacity: 0;
                    transform: translateX(-20px);
                }
                to {
                    opacity: 1;
                    transform: translateX(0);
                }
            }

            /* Application de l'animation aux lignes du corps */
            .table-Arrondissement tbody tr {
                animation: slideIn 0.5s ease-in-out;
                animation-fill-mode: both;
            }

            /* Délai d'animation pour chaque ligne */
            .table-Arrondissement tbody tr:nth-child(1) { animation-delay: 0.1s; }
            .table-Arrondissement tbody tr:nth-child(2) { animation-delay: 0.2s; }
            .table-Arrondissement tbody tr:nth-child(3) { animation-delay: 0.3s; }
            .table-Arrondissement tbody tr:nth-child(4) { animation-delay: 0.4s; }
            .table-Arrondissement tbody tr:nth-child(5) { animation-delay: 0.5s; }
            .table-Arrondissement tbody tr:nth-child(6) { animation-delay: 0.6s; }
            .table-Arrondissement tbody tr:nth-child(7) { animation-delay: 0.7s; }
            .table-Arrondissement tbody tr:nth-child(8) { animation-delay: 0.8s; }
            .table-Arrondissement tbody tr:nth-child(9) { animation-delay: 0.9s; }
            .table-Arrondissement tbody tr:nth-child(10) { animation-delay: 1s; }
            .table-Arrondissement tbody tr:nth-child(11) { animation-delay: 1.1s; }
            .table-Arrondissement tbody tr:nth-child(12) { animation-delay: 1.2s; }
        
        /* Style de base pour la table */
            .table-Facture {
                width: 100%;
                border-collapse: collapse;
                margin: 25px 0;
                font-size: 0.9em;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                min-width: 400px;
                box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
                animation: fadeIn 1s ease-in-out;
            }

            /* Animation d'apparition de la table */
            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: translateY(20px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            /* Style de l'en-tête de la table */
            .table-Facture thead tr {
                background-color: #009879;
                color: #ffffff;
                text-align: left;
            }

            /* Style des cellules de l'en-tête */
            .table-Facture th {
                padding: 12px 15px;
                font-weight: bold;
                text-transform: uppercase;
                letter-spacing: 0.1em;
            }

            /* Style des cellules du corps de la table */
            .table-Facture td {
                padding: 12px 15px;
                border-bottom: 1px solid #dddddd;
                transition: background-color 0.3s ease;
            }

            /* Style des lignes impaires */
            .table-Facture tbody tr:nth-of-type(odd) {
                background-color: #f3f3f3;
            }

            /* Style des lignes au survol */
            .table-Facture tbody tr:hover {
                background-color: #f1f1f1;
                transform: scale(1.02);
                transition: transform 0.3s ease, background-color 0.3s ease;
            }

            /* Style pour la colonne "Payement" */
            .table-Facture tbody tr td:last-child {
                font-weight: bold;
                color: #009879;
            }

            /* Animation pour l'apparition des lignes */
            @keyframes slideIn {
                from {
                    opacity: 0;
                    transform: translateX(-20px);
                }
                to {
                    opacity: 1;
                    transform: translateX(0);
                }
            }

            /* Application de l'animation aux lignes du corps */
            .table-Facture tbody tr {
                animation: slideIn 0.5s ease-in-out;
                animation-fill-mode: both;
            }

            /* Délai d'animation pour chaque ligne */
            .table-Facture tbody tr:nth-child(1) { animation-delay: 0.1s; }
            .table-Facture tbody tr:nth-child(2) { animation-delay: 0.2s; }
            .table-Facture tbody tr:nth-child(3) { animation-delay: 0.3s; }
            .table-Facture tbody tr:nth-child(4) { animation-delay: 0.4s; }
            .table-Facture tbody tr:nth-child(5) { animation-delay: 0.5s; }
            .table-Facture tbody tr:nth-child(6) { animation-delay: 0.6s; }
            .table-Facture tbody tr:nth-child(7) { animation-delay: 0.7s; }
            .table-Facture tbody tr:nth-child(8) { animation-delay: 0.8s; }
            .table-Facture tbody tr:nth-child(9) { animation-delay: 0.9s; }
            .table-Facture tbody tr:nth-child(10) { animation-delay: 1s; }
            .table-Facture tbody tr:nth-child(11) { animation-delay: 1.1s; }
            .table-Facture tbody tr:nth-child(12) { animation-delay: 1.2s; }



            /* ###########################################################################################################3 */
        
        .modal-facture-fille {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(248, 245, 245, 0.5);
            display: flex;
            align-items: center;
            justify-content: center;
            z-index: 1000;
        }

        .modal-facture-fille-content {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            width: 400px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
            text-align: left;
            position: relative;
        }

        .modal-facture-fille-close {
            position: absolute;
            top: 10px;
            right: 10px;
            font-size: 20px;
            cursor: pointer;
            color: #333;
        }

        .modal-facture-fille-content h3 {
            margin-top: 0;
        }

        .modal-facture-fille-content p {
            margin: 10px 0;
        }
    </style>


</head>
<body>
    <div id="map"></div>
    
    <!-- Boîte modale pour le formulaire -->
    <div id="formModal" class="modal">
        <div class="modal-header">
            Insertion Maison
            <span id="closeModal" style="float: right; cursor: pointer; color: red; font-weight: bold;">&times;</span>
        </div>
        <form id="houseForm">
            <label for="nom">Nom :</label>
            <input type="text" id="nom" name="nom" required>

            <label for="longueur">Longueur :</label>
            <input type="number" id="longueur" name="longueur" required>

            <label for="largeur">Largeur :</label>
            <input type="number" id="largeur" name="largeur" required>

            <label for="nbEtage">Nombre d'étages :</label>
            <input type="number" id="nbEtage" name="nbEtage" required>

            <label for="mur">Type du mur :</label>
            <select id="mur" name="mur" required>
                <% for (int i = 0; i < listeMur.size(); i++) { %>
                    <option value="<%= listeMur.elementAt(i).getNomCaracteristique()%>"><%= listeMur.elementAt(i).getNomCaracteristique() %></option>  
                <% } %>
            </select>

            <label for="toit">Type du toit :</label>
            <select id="toit" name="toit" required>
                <% for (int i = 0; i < listeToit.size(); i++) { %>
                    <option value="<%= listeToit.elementAt(i).getNomCaracteristique()%>"><%= listeToit.elementAt(i).getNomCaracteristique() %></option>  
                <% } %>
            </select>

            <button type="submit">Valider</button>
        </form>
    </div>
            
    <!-- Boîte modale pour le formulaire 2 -->
    <div id="formModal2" class="modal petitModal">
        <div class="modal-header">
            Payer les Impôts
            <span id="closeModal2" style="float: right; cursor: pointer; color: red; font-weight: bold;">&times;</span>
        </div>
        <form id="houseForm2">
            <input type="hidden" id="idMaisonPayer" name="idMaisonPayer" required>

            <label for="annee">Pour l'années :</label>
            <input type="number" id="annee" name="annee" required>

            <label for="nbMois">Nombre des mois à payer :</label>
            <input type="number" id="nbMois" name="nbMois" required>
            
            <button type="submit">Payer</button>
        </form>
    </div>
    
    <!-- Boîte modale pour le formulaire 3-->
    <div id="formModal3" class="modal">
        <div class="sisiny">
            <span id="closeModal3" style="float: right; cursor: pointer; color: red; font-weight: bold;">&times;</span>
        </div>
        <div class="modal-header">
            
        </div>
        <form id="houseForm3">
            <input type="hidden" id="idMaisonPayerListe" name="idMaisonPayerListe" required>
            <label for="anneeListe">Pour l'années :</label>
            <input type="number" id="anneeListe" name="anneeListe" required>

            <button type="submit">Filtrer</button>
        </form>
        <table id="impotsTable">
            <thead>
                <tr>
                    <th>Mois payés</th>
                    <th>Années</th>
                    <th>Impôts du mois</th>
                </tr>
            </thead>
            <tbody>
                <!-- Les lignes dynamiques seront insérées ici -->
            </tbody>
            <tfoot>
                <!-- Le pied de tableau sera généré ici -->
            </tfoot>
        </table>
    </div>
    
    <div class="container-Arrondissement">
        <h1 class="title-Arrondissement">Filtrage par Arrondissement pour l'années:</h1>
        <form id="formArrondissementFiltre">
            <input type="number" id="anneeArrondissemnet" name="anneeArrondissemnet" required>
            <button type="submit">Filtrer</button>
        </form>
        <table class="table-Arrondissement">
            <thead>
                <tr>
                    <th>id-Ar</th>
                    <th>Nom de l'arrondissement</th>
                    <th>Années</th>
                    <th>Nb Maison</th>
                    <th>Somme Impôts Payer</th>
                    <th>Somme Impôts Non Payer</th>
                </tr>
            </thead>
            <tbody>
                <!-- Les lignes dynamiques seront insérées ici -->
            </tbody>
        </table>
    </div>
    
    <div class="container-Arrondissement">
        <h1 class="title-Arrondissement">Liste des Factures disponnible dans <%= rel.getNomCommune() %> :</h1>
        <form id="formFactureFiltre">
            
            <select id="moisFacture" name="moisFacture">
                <option selected disabled>Mois</option>
                <option value="1">Janvier</option>
                <option value="2">Fevrier</option>
                <option value="3">Mars</option>
                <option value="4">Avril</option>
                <option value="5">Mai</option>
                <option value="6">Juin</option>
                <option value="7">Juillet</option>
                <option value="8">Aôut</option>
                <option value="9">Septembre</option>
                <option value="10">Octobre</option>
                <option value="11">Novembre</option>
                 <option value="12">Decembre</option>
                
            </select>
            <input type="number" id="anneeFacture" name="anneeFacture" required style="margin-left: 10px" placeholder="Années">
            <button type="submit">Calculer</button>
        </form>
        <table class="table-Facture">
            <thead>
                <tr>
                    <!-- <th>ID Maison</th> -->
                    <th>Nom Maison</th>
                    <th>Mois</th>
                    <th>Années</th>
                    <th>Montant</th>
                    <th>Payement</th>
                </tr>
            </thead>
            <tbody>
                <!-- Les lignes dynamiques seront insérées ici -->
            </tbody>
        </table>
        
        <div class="modal-facture-fille" style="display: none;">
            <div class="modal-facture-fille-content">
                <span class="modal-facture-fille-close">&times;</span>
                <h3 id="modal-facture-fille-title"></h3>
                <p><strong>PU :</strong> <span id="facture-pu"></span></p>
                <p><strong>Surface Total :</strong> <span id="facture-surfaceTotal"></span></p>
                <p><strong>Coefficient Mure :</strong> <span id="facture-coefficientMure"></span></p>
                <p><strong>Coefficient Toit :</strong> <span id="facture-coefficientToit"></span></p>
                <p><strong>Proprietaire :</strong> <span id="facture-proprietaire"></span></p>
            </div>
        </div>
    </div>
    

    <script>
        function creeXHR() {
            var xhr;
            try {
                xhr = new ActiveXObject('Msxml2.XMLHTTP');
            } catch (e) {
                try {
                    xhr = new ActiveXObject('Microsoft.XMLHTTP');
                } catch (e2) {
                    try {
                        xhr = new XMLHttpRequest();
                    } catch (e3) {
                        xhr = false;
                    }
                }
            }
            return xhr;
        }

        function mettreAJourTable(data) {
            // Sélectionner le corps du tableau
            var tbody = document.querySelector('.table-Arrondissement tbody');
            // Vider le tableau existant
            tbody.innerHTML = "";

            // Parcourir les données reçues
            data.forEach(function (arrondissement) {
                // Créer une nouvelle ligne
                var row = document.createElement('tr');

                // Ajouter les colonnes (cellules) avec une concaténation classique
                var rowContent = 
                    "<td>" + arrondissement.id + "</td>" +
                    "<td>" + arrondissement.nomArrondissement + "</td>" +
                    "<td>" + arrondissement.annees + "</td>" +
                    "<td>" + arrondissement.nbMaison + "</td>" +
                    "<td>" + arrondissement.sommePayer.toFixed(2) + "</td>" +
                    "<td>" + arrondissement.sommeNonPayer.toFixed(2) + "</td>";

                // Insérer le contenu dans la ligne
                row.innerHTML = rowContent;

                // Ajouter la ligne au tableau
                tbody.appendChild(row);
            });
        }

        var formulaireFiltre = document.getElementById('formArrondissementFiltre');
        formulaireFiltre.onsubmit = function (event) {
            event.preventDefault();

            // Récupérer les données du formulaire
            var anneeArrondissemnet = document.getElementById('anneeArrondissemnet').value;
            
            // Préparer les données pour la requête POST
            var formData = new FormData();
            formData.append("anneeArrondissemnet", anneeArrondissemnet);


            var xhr = creeXHR();
            // Configurer la requête POST avec l'URL de la servlet
            xhr.open("POST", "http://localhost:1010/station/FiltreArrondissementServlet", true);
            // Ajouter un événement pour gérer la réponse
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) { // Vérifie si la requête est terminée
                    if (xhr.status === 200) { // Vérifie si le statut HTTP est OK
                        try {
                            // Parse la réponse JSON
                            var data = JSON.parse(xhr.responseText);
                            // Appeler une fonction pour mettre à jour le tableau
                            mettreAJourTable(data);
                        } catch (e) {
                            console.error("Erreur lors de l'analyse de la réponse JSON :", e);
                        }
                    } else {
                        console.error("Erreur lors de la requête :", xhr.status, xhr.statusText);
                        alert("Erreur lors de la requête :", xhr.status, xhr.statusText);
                    }
                }
            };
            xhr.send(formData);

            // Réinitialiser le formulaire
            formulaireFiltre.reset();
        };
        
        
        function mettreAJourTableFacture(data) {
            // Sélectionner le corps du tableau
            var tbody = document.querySelector('.table-Facture tbody');
            var moisString = ["Invalide","Janvier","Fevrier","Mars","Avril","Mai","Juin","Juillet","Aôut","Septembre","Octobre","Novembre","Decembre"];
            var payementFacture = ["Non Payer","Payer"];
            var modal = document.querySelector('.modal-facture-fille');
            var modalClose = document.querySelector('.modal-facture-fille-close');
            var modalTitle = document.getElementById('modal-facture-fille-title');

            // Références aux éléments pour afficher les données dans la modale
            var facturePU = document.getElementById('facture-pu');
            var factureSurfaceTotal = document.getElementById('facture-surfaceTotal');
            var factureCoefficientMure = document.getElementById('facture-coefficientMure');
            var factureCoefficientToit = document.getElementById('facture-coefficientToit');
            var factureProprietaire = document.getElementById('facture-proprietaire');
            
            // Vider le tableau existant
            tbody.innerHTML = "";

            var misy = 0;
            // Parcourir les données reçues
            data.forEach(function(facture) {
                misy = 1;
                // Créer une nouvelle ligne pour la FactureMere
                var row = document.createElement('tr');

                // Ajouter les colonnes (cellules) avec les propriétés de 'factMere'

                var rowContent = 
                    // "<td>" + facture.factMere.id_maison + "</td>" + 
                    "<td>" + facture.factMere.nom_maison + "</td>" + 
                    "<td>" + moisString[facture.factMere.mois] + "</td>" +    
                    "<td>" + facture.factMere.annees + "</td>" +  
                    "<td>" + facture.factMere.montant.toFixed(2) + "</td>" +  
                    "<td>" + payementFacture[facture.factMere.etatPaye] + "</td>" ;

                // Insérer le contenu dans la ligne
                row.innerHTML = rowContent;

                // Ajouter un événement de clic pour afficher la modale
                row.addEventListener('click', function () {
                    var phrase = "Détails de la Facture pour "+facture.factMere.nom_maison +"("+moisString[facture.factMere.mois]+" "+facture.factMere.annees +")";
                    modalTitle.textContent = phrase;
                    
                    // Remplir les champs de la modale avec les données de FactureFille
                    facturePU.textContent = facture.factFille.pu.toFixed(2);
                    factureSurfaceTotal.textContent = facture.factFille.surfaceTotal.toFixed(2);
                    factureCoefficientMure.textContent = facture.factFille.coefficientMure.toFixed(2);
                    factureCoefficientToit.textContent = facture.factFille.coefficientToit.toFixed(2);
                    factureProprietaire.textContent = facture.factFille.proprietaire;

                    // Afficher la modale
                    modal.style.display = "flex";
                });

                // Ajouter la ligne au tableau
                tbody.appendChild(row);
            });
            
            if(misy == 0){
                alert("Aucune facture disponible car il n'y a pas de maison");
            }
            
            // Ajouter un événement pour fermer la modale
            modalClose.addEventListener('click', function () {
                modal.style.display = "none";
            });
            
            // Fermer la modale si l'utilisateur clique en dehors du contenu
            window.addEventListener('click', function (event) {
                if (event.target === modal) {
                    modal.style.display = "none";
                }
            });
        }
        
        var formFactureFiltre = document.getElementById('formFactureFiltre');
        formFactureFiltre.onsubmit = function (event){
            event.preventDefault();

            // Récupérer les données du formulaire
            var moisFacture = document.getElementById('moisFacture').value;
            var anneeFacture = document.getElementById('anneeFacture').value;
            var idCom = <%= id_commune %>;
            
            // Préparer les données pour la requête POST
            var formData = new FormData();
            formData.append("moisFacture", moisFacture);
            formData.append("anneeFacture", anneeFacture);
            formData.append("idCom", idCom);


            var xhr = creeXHR();
            // Configurer la requête POST avec l'URL de la servlet
            xhr.open("POST", "http://localhost:1010/station/CalculerFactureServlet", true);
            // Ajouter un événement pour gérer la réponse
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) { // Vérifie si la requête est terminée
                    if (xhr.status === 200) { // Vérifie si le statut HTTP est OK
                        try {
                            // Parse la réponse JSON
                            var data = JSON.parse(xhr.responseText);
                            mettreAJourTableFacture(data);
                        } catch (e) {
                            console.error("Erreur lors de l'analyse de la réponse JSON :", e);
                        }
                    } else {
                        console.error("Erreur lors de la requête :", xhr.status, xhr.statusText);
                        alert("Erreur lors de la requête :", xhr.status, xhr.statusText);
                    }
                }
            };
            xhr.send(formData);

            // Réinitialiser le formulaire
            formFactureFiltre.reset();
        };


        function loadArrondissements() {
            var xhr = creeXHR();
            // Configurer la requête POST avec l'URL de la servlet
            xhr.open("POST", "http://localhost:1010/station/ArrondissementServlet", true);

            // Ajouter un événement pour gérer la réponse
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4 && xhr.status == 200) {
                    // Réponse reçue avec succès
                    var arrondissements = JSON.parse(xhr.responseText);

                    arrondissements.forEach(function(arrondissement) {
                        var zoneWKT = arrondissement.zone;

                        // Extraire les coordonnées du WKT (on retire "POLYGON" et parenthèses inutiles)
                        var rawCoordinates = zoneWKT
                            .replace("POLYGON ((", "")
                            .replace("))", "")
                            .split(", ");

                        // Convertir les coordonnées en tableau de paires de nombres
                        var coordinates = rawCoordinates.map(function(coord) {
                            var parts = coord.split(" ");
                            return [parseFloat(parts[1]), parseFloat(parts[0])]; // [latitude, longitude]
                        });

                        // Créer un polygone Leaflet à partir des coordonnées
                        var polygon = L.polygon(coordinates, { color: 'blue' });

                        // Ajouter le polygone à la carte
                        polygon.addTo(map);

                        // Gérer l'événement de clic droit pour afficher la popup
                        polygon.on('contextmenu', function (e) {
                            L.popup()
                                .setLatLng(e.latlng)
                                .setContent(
                                    "<strong>Arrondissement :</strong> " + arrondissement.nom + "<br>" +
                                    "<strong>Coordonnées :</strong><br>" + rawCoordinates.join("<br>")
                                )
                                .openOn(map);
                        });
                    });
                } else if (xhr.readyState == 4) {
                    console.error("Erreur lors de la requête : ", xhr.status);
                }
            };
            // Envoyer la requête POST
            xhr.send();
        }

        function loadCommune() {
            var isCommune = <%= id_commune %>;
            
            var xhr = creeXHR();
            // Configurer la requête POST avec l'URL de la servlet
            xhr.open("POST", "http://localhost:1010/station/CommuneServlet", true);

            // Ajouter un événement pour gérer la réponse
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4 && xhr.status == 200) {
                    // Réponse reçue avec succès
                    var arrondissements = JSON.parse(xhr.responseText);

                    arrondissements.forEach(function(arrondissement) {
                        var zoneWKT = arrondissement.zone;

                        // Extraire les coordonnées du WKT (on retire "POLYGON" et parenthèses inutiles)
                        var rawCoordinates = zoneWKT
                            .replace("POLYGON ((", "")
                            .replace("))", "")
                            .split(", ");

                        // Convertir les coordonnées en tableau de paires de nombres
                        var coordinates = rawCoordinates.map(function(coord) {
                            var parts = coord.split(" ");
                            return [parseFloat(parts[1]), parseFloat(parts[0])]; // [latitude, longitude]
                        });

                        // Déterminer la couleur et le comportement selon la condition
                        var color = arrondissement.id === isCommune ? '' : '';
                        var interactive = arrondissement.id === isCommune; // Permet de cliquer seulement si id === isCommune

                        
                        // Créer un polygone Leaflet à partir des coordonnées
                        var polygon = L.polygon(coordinates, { color: color, interactive: interactive });

                        // Ajouter le polygone à la carte
                        polygon.addTo(map);

                        // Gérer l'événement de clic droit pour afficher la popup
                        polygon.on('contextmenu', function (e) {
                            L.popup()
                                .setLatLng(e.latlng)
                                .setContent(
                                    "<strong>Commune :</strong> " + arrondissement.nom + "<br>" +
                                    "<strong>Coordonnées :</strong><br>" + rawCoordinates.join("<br>")
                                )
                                .openOn(map);
                        });
                    });
                } else if (xhr.readyState == 4) {
                    console.error("Erreur lors de la requête : ", xhr.status);
                }
            };
            // Envoyer la requête POST
            xhr.send();
        }
        
        // Variables globales pour stocker la latitude et la longitude
        let lat = null;
        let lng = null;
        
        // Créer la carte centrée sur Madagascar
        var map = L.map('map', {
            center: [-18.9, 46.6], // Centre géographique de Madagascar
            zoom: 7.5, // Niveau de zoom initial
            zoomControl: false // Désactiver les contrôles de zoom par défaut
        });
        
        // Ajouter les tuiles OpenStreetMap
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19,
            attribution: '© OpenStreetMap contributors'
        }).addTo(map);

        // Ajouter des contrôles de zoom à droite
        L.control.zoom({
            position: 'topright' // Déplace les boutons de zoom à droite
        }).addTo(map);
        
        

        // Load image icon maison

        <% for (int i = 0; i < listeMaison.size(); i++) { 
            Maison maison = listeMaison.elementAt(i); %>
            const iconMaison<%= i %> = L.icon({
                iconUrl: 'https://cdn-icons-png.flaticon.com/512/619/619153.png', // Remplace par le chemin de ton fichier PNG
                iconSize: [30, 30], // Dimensions de l'image (largeur, hauteur)
                iconAnchor: [15, 30], // Point d'ancrage de l'icône (bas-centre)
                popupAnchor: [0, -30] // Position du popup par rapport à l'icône
            });

            L.marker([<%= maison.getLat() %>, <%= maison.getLog() %>], { icon: iconMaison<%= i %> })
                .addTo(map)
                .bindPopup(`
                    <div>
                        <strong>Nom :</strong> <%= maison.getNom() %><br>
                        <strong>Dimensions :</strong> <%= maison.getLongueur() %>m x <%= maison.getLargeur() %>m<br>
                        <strong>Nombre d'étages :</strong> <%= maison.getNb_etage() %><br>
                        <strong>Propriétaire :</strong> <%= maison.getPropio() %><br>
                        <div style="display:inline-block; margin-top: 10px;">
                            <button type="button" onclick="openModal(event, <%= maison.getId() %>)">Payer Impôts</button>
                            <button type="button" onclick="openModalListe(this)" data-maison-id="<%= maison.getId() %>" data-maison-nom="<%= maison.getNom() %>" style="margin-left:10px;">Listes des Impôts</button>
                        </div>
                    </div>      
                `);
        <% } %>


            
            
        // Charger les arrondissements et communes
        loadCommune();
        loadArrondissements();

        
        //Gestion de la bôite modale 2
        var formModal2 = document.getElementById('formModal2');
        var houseForm2 = document.getElementById('houseForm2');
        var closeModal2 = document.getElementById('closeModal2');
        
        //Gestion de la bôite modale 3
        var formModal3 = document.getElementById('formModal3');
        var houseForm3 = document.getElementById('houseForm3');
        var closeModal3 = document.getElementById('closeModal3');
        
        // Fonction pour ouvrir la modale
        function openModal(event, maisonId) {
            const button = event.target; // Récupérer le bouton cliqué
            const modal = document.getElementById('formModal2'); // La modale
            const docIdMaisonPayer = document.getElementById('idMaisonPayer'); // Champ caché pour l'ID

            // Définir la position dynamique de la modale
            const rect = button.getBoundingClientRect();
            modal.style.top = rect.bottom + window.scrollY + 'px'; // Position en bas du bouton
            modal.style.left = rect.left + window.scrollX + 'px'; // Aligné au bouton

            // Remplir l'ID de la maison
            docIdMaisonPayer.value = maisonId;

            // Afficher la modale
            modal.style.display = 'block';
            formModal.style.display = 'none';
            formModal3.style.display = 'none';
        }
        
        
        // Fonction pour ouvrir la modale
        function openModalListe(button) {
            const modal = document.getElementById('formModal3'); // La modale
            const docIdMaisonPayerListe = document.getElementById('idMaisonPayerListe'); // Champ caché pour l'ID
            
            // Récupérer les données à partir de l'élément button
            const maisonId = button.getAttribute('data-maison-id');
            const nomMaison2 = button.getAttribute('data-maison-nom');

            // Mettre à jour le titre avec le nom de la maison
            const modalHeader = modal.querySelector('.modal-header');
            // Vérification d'une syntaxe simple pour la mise à jour
            modalHeader.innerHTML = 'Liste de tous les Impôts pour ' + nomMaison2;

            // Remplir l'ID de la maison
            docIdMaisonPayerListe.value = maisonId;

            // Afficher la modale
            modal.style.display = 'block';
            formModal.style.display = 'none';
            formModal2.style.display = 'none';
            
            const tableBody = document.querySelector("#impotsTable tbody");
            // Vider les anciennes lignes du tableau
            tableBody.innerHTML = "";
            const tableFoot = document.querySelector("#impotsTable tfoot");
            tableFoot.innerHTML = "";
        }
        
        
        // Ajouter un gestionnaire pour valider le formulaire 2
        houseForm2.onsubmit = function (event) {
            event.preventDefault();

            // Récupérer les données du formulaire
            var idMaisonImpots = document.getElementById('idMaisonPayer').value;
            var anneeImpots = document.getElementById('annee').value;
            var nbMoisImpots = document.getElementById('nbMois').value;
            
            // Préparer les données pour la requête POST
            var formData = new FormData();
            formData.append("idMaisonImpots", idMaisonImpots);
            formData.append("anneeImpots", anneeImpots);
            formData.append("nbMoisImpots", nbMoisImpots);


            var xhr = creeXHR();
            // Configurer la requête POST avec l'URL de la servlet
            xhr.open("POST", "http://localhost:1010/station/HistoriqueImpotServlet", true);
            // Ajouter un événement pour gérer la réponse
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) { // Vérifie si la requête est terminée
                    if (xhr.status === 200) { // Vérifie si le statut HTTP est OK
                        try {
                            const response = JSON.parse(xhr.responseText);
                            if (response.status === "success") {
                                alert("Insertion Impots effectuée avec succès !");
                            } else {
                                alert("Erreur: " + response.message);
                            }
                        } catch (e) {
                            console.error("Erreur de parsing JSON :", e);
                            alert("Erreur de parsing JSON :", e);
                        }

                    } else {
                        console.error("Erreur lors de la requête :", xhr.status, xhr.statusText);
                        alert("Erreur lors de la requête :", xhr.status, xhr.statusText);
                    }
                }
            };
            xhr.send(formData);

            // Cacher le formulaire
            formModal2.style.display = 'none';

            // Réinitialiser le formulaire
            houseForm2.reset();
        };
        
        
        // Ajouter un gestionnaire pour valider le formulaire 2
        houseForm3.onsubmit = function (event) {
            event.preventDefault();

            // Récupérer les données du formulaire
            var idMaisonPayerListe = document.getElementById('idMaisonPayerListe').value;
            var anneeListe = document.getElementById('anneeListe').value;
            
            // Préparer les données pour la requête POST
            var formData = new FormData();
            formData.append("idMaisonPayerListe", idMaisonPayerListe);
            formData.append("anneeListe", anneeListe);


            var xhr = creeXHR();
            // Configurer la requête POST avec l'URL de la servlet
            xhr.open("POST", "http://localhost:1010/station/RechercheHistoriqueImpotServlet", true);
            // Ajouter un événement pour gérer la réponse
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) { // Vérifie si la requête est terminée
                    if (xhr.status === 200) { // Vérifie si le statut HTTP est OK
                        try {
                            const response = JSON.parse(xhr.responseText);
                            const tableBody = document.querySelector("#impotsTable tbody");
                            const tableFoot = document.querySelector("#impotsTable tfoot");

                            // Vider les anciennes lignes du tableau
                            tableBody.innerHTML = "";
                            tableFoot.innerHTML = "";

                            let totalPrix = 0;

                            // Vérifier si la réponse contient des données
                            if (response.length > 0) {
                                response.forEach(item => {
                                    // Ajouter au total
                                    totalPrix += item.prix_mois;
                                    
                                    // Créer une nouvelle ligne
                                    const row = document.createElement("tr");

                                    // Ajouter des cellules pour chaque colonne en utilisant des concaténations
                                    row.innerHTML = "<td>" + item.nom_mois + "</td>" +
                                                    "<td>" + item.annees + "</td>" +
                                                    "<td>" + item.prix_mois.toLocaleString() + " Ar</td>";

                                    // Ajouter la ligne au corps de la table
                                    tableBody.appendChild(row);
                                });
                                
                                // Ajouter le pied de tableau avec la somme totale
                                const totalRow = document.createElement("tr");
                                totalRow.innerHTML = "<td colspan='2' style='text-align:center; font-weight:bold;'>Total</td>" +
                                                     "<td style='font-weight:bold;'>" + totalPrix.toLocaleString() + " Ar</td>";
                                tableFoot.appendChild(totalRow);
                            } else {
                                // Ajouter une ligne indiquant qu'aucune donnée n'est disponible
                                const noDataRow = document.createElement("tr");
                                noDataRow.innerHTML = `<td colspan="5" style="text-align:center;">Aucune donnée disponible</td>`;
                                tableBody.appendChild(noDataRow);
                            }
                        } catch (e) {
                           console.error("Erreur de parsing JSON :", e);
                           alert("Erreur de parsing JSON : " + e.message);
                       }
                    } else {
                        console.error("Erreur lors de la requête :", xhr.status, xhr.statusText);
                        alert("Erreur lors de la requête :", xhr.status, xhr.statusText);
                    }
                }
            };
            xhr.send(formData);
            // Réinitialiser le formulaire
            houseForm3.reset();
        };
        
        // Gestion de la boîte modale
        var formModal = document.getElementById('formModal');
        var houseForm = document.getElementById('houseForm');
        var closeModal = document.getElementById('closeModal');
        var dataTableBody = document.querySelector('#data-table tbody');

        // Ajouter un événement au clic sur la carte
        map.on('click', function (e) {
            // Obtenir les coordonnées du clic
            lat = e.latlng.lat;
            lng = e.latlng.lng;
            
            // Obtenir les coordonnées du clic dans le conteneur de la carte
            var containerPoint = map.latLngToContainerPoint(e.latlng);

            // Définir la position de la modale
            formModal.style.top = containerPoint.y + 'px';
            formModal.style.left = containerPoint.x + 'px';

            // Afficher le formulaire
            formModal.style.display = 'block';
            formModal2.style.display = 'none';
            formModal3.style.display = 'none';
        });

        // Ajouter un gestionnaire pour valider le formulaire
        houseForm.onsubmit = function (event) {
            event.preventDefault();

            // Récupérer les données du formulaire
            var nom = document.getElementById('nom').value;
            var longueur = document.getElementById('longueur').value;
            var largeur = document.getElementById('largeur').value;
            var nbEtage = document.getElementById('nbEtage').value;
            var mur = document.getElementById('mur').value;
            var toit = document.getElementById('toit').value;
            
            // Préparer les données pour la requête POST
            var formData = new FormData();
            formData.append("nom", nom);
            formData.append("longueur", longueur);
            formData.append("largeur", largeur);
            formData.append("nbEtage", nbEtage);
            formData.append("mur", mur);
            formData.append("toit", toit);
            formData.append("latitude", lat);
            formData.append("longitude", lng);


            var xhr = creeXHR();
            // Configurer la requête POST avec l'URL de la servlet
            xhr.open("POST", "http://localhost:1010/station/MaisonServlet", true);
            // Ajouter un événement pour gérer la réponse
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) { // Vérifie si la requête est terminée
                    if (xhr.status === 200) { // Vérifie si le statut HTTP est OK
                        try {
                            const response = JSON.parse(xhr.responseText);
                            if (response.status === "success") {
                                alert("Insertion effectuée avec succès !");
                                location.reload();
                            } else {
                                alert("Erreur: " + response.message);
                            }
                        } catch (e) {
                            console.error("Erreur de parsing JSON :", e);
                            alert("Erreur de parsing JSON :", e);
                        }

                    } else {
                        console.error("Erreur lors de la requête :", xhr.status, xhr.statusText);
                        alert("Erreur lors de la requête :", xhr.status, xhr.statusText);
                    }
                }
            };
            xhr.send(formData);

            // Cacher le formulaire
            formModal.style.display = 'none';

            // Réinitialiser le formulaire
            houseForm.reset();
        };

        // Ajouter un gestionnaire pour fermer la boîte modale
        closeModal.onclick = function () {
            formModal.style.display = 'none';
        };
        
        // Ajouter un gestionnaire pour fermer la boîte modale 2
        closeModal2.onclick = function () {
            formModal2.style.display = 'none';
        };
        
        // Ajouter un gestionnaire pour fermer la boîte modale 3
        closeModal3.onclick = function () {
            formModal3.style.display = 'none';
        };

        // Fermer la modale en cliquant ailleurs
        window.onclick = function (event) {
            if (event.target === formModal) {
                formModal.style.display = 'none';
            }
            if(event.target === formModal2){
                formModal2.style.display = 'none';
            }
            if(event.target === formModal3){
                formModal3.style.display = 'none';
            }
        };
    
    </script>

</body>
</html>


