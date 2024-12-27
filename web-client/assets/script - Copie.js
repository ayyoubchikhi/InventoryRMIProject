document.addEventListener('DOMContentLoaded', async () => {
  const addProductForm = document.getElementById('add-product-form');
  const resultMessage = document.getElementById('result-message');

  try {
    // Vérifier la connexion RMI
    const response = await fetch('http://localhost:1099/InventoryService/ping');
    if (!response.ok) {
      throw new Error('Impossible de se connecter au serveur RMI');
    }

    addProductForm.addEventListener('submit', async (event) => {
      event.preventDefault();

      const productName = document.getElementById('product-name').value;
      const productQuantity = document.getElementById('product-quantity').value;
      const productPrice = document.getElementById('product-price').value;

      try {
        const response = await fetch('addProduct', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ name: productName, quantity: productQuantity, price: productPrice })
        });

        if (response.ok) {
          resultMessage.textContent = 'Produit ajouté avec succès !';
          resultMessage.style.color = 'green';
          addProductForm.reset();
        } else {
          const error = await response.json();
          resultMessage.textContent = `Erreur : ${error.message}`;
          resultMessage.style.color = 'red';
        }
      } catch (error) {
        resultMessage.textContent = `Erreur : ${error.message}`;
        resultMessage.style.color = 'red';
      }
    });
  } catch (error) {
    resultMessage.textContent = `Erreur de connexion : ${error.message}`;
    resultMessage.style.color = 'red';
  }
});