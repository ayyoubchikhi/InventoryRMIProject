package client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import server.InventoryInterface;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ClientGUI extends Application {
    private InventoryInterface inventoryService;
    private TextArea productListArea;
    private VBox mainContent;
    private ScrollPane scrollPane;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            connectToServer();
            if (!showLoginScreen()) {
                System.exit(0);
            }
            setupMainWindow(primaryStage);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur de connexion au serveur", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void connectToServer() throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        inventoryService = (InventoryInterface) registry.lookup("InventoryService");
    }

    private void setupMainWindow(Stage primaryStage) {
        primaryStage.setTitle("Système d'Inventaire");
        
        // Layout principal avec scroll
        mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.TOP_CENTER);
        
        // Ajout du logo et du titre
        VBox logoContainer = new VBox(10);
        logoContainer.getStyleClass().add("logo-container");
        
        try {
            ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/resources/logo.png")));
            logo.setFitHeight(100);
            logo.setFitWidth(100);
            logo.setPreserveRatio(true);
            logoContainer.getChildren().add(logo);
        } catch (Exception e) {
            System.out.println("Logo non trouvé");
        }
        
        Label title = new Label("Système de Gestion d'Inventaire");
        title.getStyleClass().add("app-title");
        
        logoContainer.getChildren().add(title);
        mainContent.getChildren().add(logoContainer);
        
        scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Création des sections
        VBox productSection = createSection("Gestion des Produits", 
            createAddProductSection(),
            createUpdateQuantitySection(),
            createDeleteProductSection()
        );

        VBox searchSection = createSection("Recherche", createSearchSection());
        VBox listSection = createSection("Liste des Produits", createListSection());

        mainContent.getChildren().addAll(productSection, searchSection, listSection);

        Scene scene = new Scene(scrollPane, 800, 900);
        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Center the window on screen
        primaryStage.centerOnScreen();
        
        refreshProductList();
    }



    private VBox createSection(String title, Node... content) {
        VBox section = new VBox(10);
        section.getStyleClass().add("section");
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("section-title");
        
        section.getChildren().add(titleLabel);
        section.getChildren().addAll(content);
        
        return section;
    }

    private GridPane createAddProductSection() {
        GridPane grid = createStyledGridPane();

        grid.add(new Label("Nom:"), 0, 0);
        TextField nameField = new TextField();
        grid.add(nameField, 1, 0);

        grid.add(new Label("Catégorie:"), 0, 1);
        TextField categoryField = new TextField();
        grid.add(categoryField, 1, 1);

        grid.add(new Label("Quantité:"), 0, 2);
        TextField quantityField = new TextField();
        grid.add(quantityField, 1, 2);

        grid.add(new Label("Prix:"), 0, 3);
        TextField priceField = new TextField();
        grid.add(priceField, 1, 3);

        Button addButton = new Button("Ajouter");
        addButton.getStyleClass().add("action-button");
        addButton.setOnAction(e -> handleAddProduct(nameField, categoryField, quantityField, priceField));
        grid.add(addButton, 1, 4);

        return grid;
    }

    private GridPane createUpdateQuantitySection() {
        GridPane grid = createStyledGridPane();

        grid.add(new Label("Nom du Produit:"), 0, 0);
        TextField nameField = new TextField();
        grid.add(nameField, 1, 0);

        grid.add(new Label("Nouvelle Quantité:"), 0, 1);
        TextField quantityField = new TextField();
        grid.add(quantityField, 1, 1);

        Button updateButton = new Button("Mettre à Jour");
        updateButton.getStyleClass().add("action-button");
        updateButton.setOnAction(e -> handleUpdateQuantity(nameField, quantityField));
        grid.add(updateButton, 1, 2);

        return grid;
    }

    private GridPane createDeleteProductSection() {
        GridPane grid = createStyledGridPane();

        grid.add(new Label("Nom du Produit:"), 0, 0);
        TextField nameField = new TextField();
        grid.add(nameField, 1, 0);

        Button deleteButton = new Button("Supprimer");
        deleteButton.getStyleClass().add("action-button");
        deleteButton.setOnAction(e -> handleDeleteProduct(nameField));
        grid.add(deleteButton, 1, 1);

        return grid;
    }

    private VBox createSearchSection() {
        VBox searchBox = new VBox(10);
        searchBox.setPadding(new Insets(10));
        searchBox.setAlignment(Pos.TOP_CENTER);

        HBox searchControls = new HBox(10);
        searchControls.setAlignment(Pos.CENTER);

        ComboBox<String> searchType = new ComboBox<>();
        searchType.getItems().addAll("Nom", "Catégorie", "Stock");
        searchType.setValue("Nom");
        searchType.getStyleClass().add("search-combo-box");

        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher...");
        searchField.setPrefWidth(200);

        Button searchButton = new Button("Rechercher");
        searchButton.getStyleClass().add("action-button");
        
        searchControls.getChildren().addAll(searchType, searchField, searchButton);
        
        TextArea searchResults = new TextArea();
        searchResults.setEditable(false);
        searchResults.setPrefRowCount(5);
        searchResults.setWrapText(true);
        searchResults.getStyleClass().add("search-results");

        searchButton.setOnAction(e -> handleSearch(searchType.getValue(), searchField.getText(), searchResults));

        searchBox.getChildren().addAll(searchControls, searchResults);
        return searchBox;
    }

    private VBox createListSection() {
    	VBox listBox = new VBox(15);  // Espacement plus grand entre les éléments
    	listBox.setPadding(new Insets(15, 20, 15, 20));  // Marge autour du VBox
    
    	// Bouton avec style amélioré
    	Button refreshButton = new Button("Actualiser la Liste");
    	refreshButton.getStyleClass().add("action-button");
   	refreshButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
    	refreshButton.setOnAction(e -> refreshProductList());
    
    	// Changez l'apparence du TextArea
   	productListArea = new TextArea();
    	productListArea.setEditable(false);
    	productListArea.setPrefRowCount(20); // Nombre de lignes du TextArea
    	productListArea.setPrefWidth(700);   // Largeur du TextArea
    	productListArea.setWrapText(true);   // Permet le retour à la ligne automatique
    	productListArea.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-font-size: 12px; -fx-padding: 10;");
   	productListArea.getStyleClass().add("product-list");

    	// Ajouter les éléments au VBox
    	listBox.getChildren().addAll(refreshButton, productListArea);
    
    	return listBox;
    }

    private boolean showLoginScreen() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Connexion");
        dialog.setHeaderText("Veuillez vous connecter");
        dialog.getDialogPane().getStyleClass().add("login-dialog");

        GridPane grid = createStyledGridPane();

        TextField usernameField = new TextField();
        usernameField.setPromptText("Nom d'utilisateur");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");

        grid.add(new Label("Utilisateur:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Mot de passe:"), 0, 1);
        grid.add(passwordField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        
        ButtonType loginButtonType = new ButtonType("Connexion", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Style the login button
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.getStyleClass().add("action-button");

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                try {
                    return inventoryService.authenticate(
                        usernameField.getText().trim(),
                        passwordField.getText().trim()
                    );
                } catch (Exception e) {
                    showAlert("Erreur", "Erreur d'authentification", Alert.AlertType.ERROR);
                    return false;
                }
            }
            return false;
        });

        return dialog.showAndWait().orElse(false);
    }

    private void handleAddProduct(TextField nameField, TextField categoryField, 
                                TextField quantityField, TextField priceField) {
        try {
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());

            inventoryService.addProduct(name, category, quantity, price);
            showAlert("Succès", "Produit ajouté avec succès", Alert.AlertType.INFORMATION);
            refreshProductList();
            clearFields(nameField, categoryField, quantityField, priceField);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs numériques valides", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout du produit", Alert.AlertType.ERROR);
        }
    }

    private void handleUpdateQuantity(TextField nameField, TextField quantityField) {
        try {
            String name = nameField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText().trim());

            inventoryService.updateProductQuantity(name, quantity);
            showAlert("Succès", "Quantité mise à jour avec succès", Alert.AlertType.INFORMATION);
            refreshProductList();
            clearFields(nameField, quantityField);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer une quantité valide", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la mise à jour", Alert.AlertType.ERROR);
        }
    }

    private void handleDeleteProduct(TextField nameField) {
        try {
            String name = nameField.getText().trim();
            
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Supprimer le produit");
            confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer " + name + " ?");
            
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        inventoryService.removeProduct(name);
                        showAlert("Succès", "Produit supprimé avec succès", Alert.AlertType.INFORMATION);
                        refreshProductList();
                        nameField.clear();
                    } catch (Exception e) {
                        showAlert("Erreur", "Erreur lors de la suppression", Alert.AlertType.ERROR);
                    }
                }
            });
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la suppression", Alert.AlertType.ERROR);
        }
    }

    private void handleSearch(String searchType, String searchText, TextArea searchResults) {
        try {
            List<String> results = switch (searchType) {
                case "Nom" -> inventoryService.searchProductsByName(searchText);
                case "Catégorie" -> inventoryService.searchProductsByCategory(searchText);
                case "Stock" -> inventoryService.searchProductsByStock(
                    Integer.parseInt(searchText.trim())
                );
                default -> throw new IllegalArgumentException("Type de recherche invalide");
            };
            
            searchResults.setText(String.join("\n", results));
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer une valeur numérique valide pour le stock", 
                     Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la recherche", Alert.AlertType.ERROR);
        }
    }

    private void refreshProductList() {
        try {
            List<String> products = inventoryService.getAllProducts();
            productListArea.setText(String.join("\n", products));
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de rafraîchir la liste", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().getStyleClass().add("custom-alert");
        alert.showAndWait();
    }

    private GridPane createStyledGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);
        return grid;
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
