import { useState, useEffect } from "react";
import {
  fetchCategories,
  addCategory,
  updateCategory,
  deleteCategory,
} from "../../components/API/CategoryServiceApi";
import "./CategoryManagement.css";
function CategoryManager() {
  const [categories, setCategories] = useState([]);
  const [newCategory, setNewCategory] = useState({ name: "", image: null });
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState("");

  // Fetch categories on load
  useEffect(() => {
    fetchCategories()
      .then(setCategories)
      .catch((err) => setError(err.message))
      .finally(() => setIsLoading(false));
  }, []);

  // Handle new category input
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewCategory({ ...newCategory, [name]: value });
  };

  const handleFileChange = (e) => {
    setNewCategory({ ...newCategory, image: e.target.files[0] });
  };

  // Add category
  const handleAddCategory = (e) => {
    e.preventDefault();
    if (!newCategory.name || !newCategory.image) {
      setError("Both name and image are required.");
      return;
    }

    const formData = new FormData();
    formData.append("name", newCategory.name);
    formData.append("image", newCategory.image);

    addCategory(formData)
      .then((addedCategory) => {
        setCategories([...categories, addedCategory]);
        setNewCategory({ name: "", image: null });
        setError("");
      })
      .catch((err) => setError(err.message));
  };

  // Update category
  const handleUpdateCategory = (categoryName, updatedName) => {
    const updatedCategory = { name: updatedName };

    updateCategory(categoryName, updatedCategory)
      .then(() => {
        setCategories(
          categories.map((cat) =>
            cat.name === categoryName ? { ...cat, name: updatedName } : cat
          )
        );
        setError("");
      })
      .catch((err) => setError(err.message));
  };

  // Delete category
  const handleDeleteCategory = (categoryName) => {
    deleteCategory(categoryName)
      .then(() => {
        setCategories(categories.filter((cat) => cat.name !== categoryName));
        setError("");
      })
      .catch((err) => setError(err.message));
  };

  return (
    <div className="container">
      <h1>Category Management</h1>

      {error && <p className="error">{error}</p>}

      {/* Add New Category */}
      <div className="add-category">
        <h2>Add New Category</h2>
        <form onSubmit={handleAddCategory}>
          <input
            type="text"
            name="name"
            placeholder="Category Name"
            value={newCategory.name}
            onChange={handleInputChange}
          />
          <input
            type="file"
            name="image"
            accept="image/*"
            onChange={handleFileChange}
          />
          <button type="submit" disabled={isLoading}>
            Add Category
          </button>
        </form>
      </div>

      {/* Loading Indicator */}
      {isLoading ? (
        <div className="loading">Loading...</div>
      ) : (
        <div className="category-list">
          {categories.map((category) => (
            <div key={category.categoryName} className="category-item">
              <div className="category-display">
                <img src={category.categoryImagePath} alt={category.categoryName} />
                <h3>{category.categoryName}</h3>
                <button
                  onClick={() =>
                    handleUpdateCategory(
                      category.categoryName,
                      prompt("Enter new name:", category.categoryName)
                    )
                  }
                >
                  Edit
                </button>
                <button onClick={() => handleDeleteCategory(category.categoryName)}>
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default CategoryManager;
