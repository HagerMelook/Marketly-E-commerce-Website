import { useEffect, useState } from "react";
import "./ProductForm.css";
import { useProductContext } from "../../contexts/ProductProvider";

function ProductForm() {
  const { selectedProduct } = useProductContext();
  const isNewProduct = selectedProduct ? true : false;
  const [productData, setProductData] = useState(selectedProduct);
  const [categories, setCategories] = useState([]);
  const [message, setMessage] = useState("");

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await fetch("http://localhost:8080/vendor/categories");
        if (response.ok) {
          const data = await response.json();
          data.sort((a, b) => a.categoryName.localeCompare(b.categoryName));
          setCategories(data);
        } else {
          console.error("Failed to fetch categories");
        }
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    };

    fetchCategories();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProductData({
      ...productData,
      [name]: value,
    });
  };

  const handleImageChange = (e) => {
    const files = [...e.target.files];
    if (files.length <= 3) {
      setProductData({
        ...productData,
        images: files,
      });
      setMessage("");
    } else {
      setMessage("You can upload a maximum of 3 images.");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append("id", productData.id);
    formData.append("name", productData.name);
    formData.append("description", productData.description);
    formData.append("quantity", productData.quantity);
    formData.append("price", productData.price);
    formData.append("category", productData.category);
    formData.append("vendorId", productData.vendorId);
    productData.images.forEach((image) => {
      formData.append("images", image);
    });

    try {
      const operation = isNewProduct ? "add" : "update";
      const method = isNewProduct ? "POST" : "PUT";
      const url = `http://localhost:8080/vendor/${operation}-product`;

      const response = await fetch(url, {
        method,
        body: formData,
      });

      if (response.ok) {
        setMessage("Product added successfully!");
        setProductData(null);
        setMessage("");
      } else {
        setMessage("Failed to add product!");
      }
    } catch (error) {
      console.error("Error adding product:", error);
      setMessage("Error adding product");
    }
  };

  return (
    <div className="add-product-container">
      {isNewProduct ? <h2>Add a New Product</h2> : <h2>Modify Product</h2>}
      <form
        className="product-form-container"
        onSubmit={handleSubmit}
        encType="multipart/form-data"
      >
        <div>
          <label>Product Name</label>
          <input
            type="text"
            name="name"
            value={productData.name}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Description</label>
          <textarea
            name="description"
            value={productData.description}
            onChange={handleChange}
            required
            maxLength="500"
          />
        </div>
        {productData.images && (
          <div>
            <label>Images</label>
            <input
              type="file"
              name="images"
              accept="image/*"
              multiple
              onChange={handleImageChange}
            />
          </div>
        )}
        <div>
          <label>Quantity</label>
          <input
            type="number"
            name="quantity"
            value={productData.quantity}
            onChange={handleChange}
            required
            step="1"
            min="0"
          />
        </div>
        <div>
          <label>Price</label>
          <input
            type="number"
            name="price"
            value={productData.price}
            onChange={handleChange}
            required
            min="0"
            step="0.01"
          />
        </div>
        <div>
          <label>Category</label>
          <select
            name="category"
            value={productData.category}
            onChange={handleChange}
            required
          >
            <option value="">Select Category</option>
            {categories.map(({ categoryName }) => (
              <option key={categoryName} value={categoryName}>
                {categoryName}
              </option>
            ))}
          </select>
        </div>
        {message && <p className="error-message">{message}</p>}
        <div>
          <button type="submit">
            {isNewProduct ? "Add Product" : "Modify Product"}
          </button>
        </div>
      </form>
    </div>
  );
}

export default ProductForm;
