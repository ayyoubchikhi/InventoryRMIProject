document.getElementById("add-product-form").addEventListener("submit", function (event) {
  event.preventDefault();
  const name = document.getElementById("product-name").value.trim();
  const quantity = parseInt(document.getElementById("product-quantity").value, 10);
  const price = parseFloat(document.getElementById("product-price").value);

  if (!name || quantity <= 0 || price <= 0) {
    document.getElementById("result-message").textContent =
      "Veuillez remplir tous les champs correctement.";
    return;
  }

  // Simule une requête au serveur
  document.getElementById("result-message").textContent =
    "Produit ajouté avec succès.";
});
