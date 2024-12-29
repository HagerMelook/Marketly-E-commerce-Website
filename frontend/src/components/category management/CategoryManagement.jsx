import { useState, useEffect } from "react";
import CategoryService from "../API/CategoryServiceApi";
import "./CategoryManagement.css";

function CategoryManagement() {
  const [categories, setCategories] = useState([]);
  const [newCategory, setNewCategory] = useState({ name: "", file: null });
  const [isLoading, setIsLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState(false);
  const [error, setError] = useState("");
  const [editingCategory, setEditingCategory] = useState(null);
  const [editForm, setEditForm] = useState({
    newName: "",
    newImage: null
  });
  const [fallbackImage] = useState('/placeholder-category.png'); // Add fallback image

  const fetchCategories = async () => {
    try {
      setIsLoading(true);
      const data = await CategoryService.getAllCategories();
      setCategories(data);
      setError("");
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewCategory({ ...newCategory, [name]: value });
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setNewCategory({ ...newCategory, file });
  };

  const handleEditFileChange = (e) => {
    const file = e.target.files[0];
    setEditForm({ ...editForm, newImage: file });
  };

  const handleAddCategory = async (e) => {
    e.preventDefault();
    if (!newCategory.name.trim() || !newCategory.file) {
      setError("Both category name and image are required");
      return;
    }

    setActionLoading(true);
    try {
      await CategoryService.addCategory(newCategory.name, newCategory.file);
      await fetchCategories();
      setNewCategory({ name: "", file: null });
      setError("");
    } catch (err) {
      setError(err.message);
    } finally {
      setActionLoading(false);
    }
  };

  const handleUpdateCategory = async (categoryName) => {
    if (!editForm.newName.trim()) {
      setError("Category name is required");
      return;
    }

    setActionLoading(true);
    try {
      const categoryDTO = {
        categoryName: editForm.newName,
        images: editForm.newImage // Only include if there's a new image
      };
      
      await CategoryService.updateCategory(categoryName, categoryDTO);
      await fetchCategories();
      setEditingCategory(null);
      setEditForm({ newName: "", newImage: null });
      setError("");
    } catch (err) {
      setError(err.message);
    } finally {
      setActionLoading(false);
    }
  };

  const handleDeleteCategory = async (categoryName) => {
    setActionLoading(true);
    try {
      await CategoryService.deleteCategory(categoryName);
      await fetchCategories();
      setError("");
    } catch (err) {
      setError(err.message);
    } finally {
      setActionLoading(false);
    }
  };

  const handleImageError = (e) => {
    e.target.src = fallbackImage;
  };

  const getImageUrl = (imagePath) => {
    return imagePath ? `http://localhost:8080/images/${imagePath}` : fallbackImage;
  };

  return (
    <div className="container">
      <h1>Category Management</h1>

      {error && <div className="error-message">{error}</div>}

      <div className="add-category-form">
        <h2>Add New Category</h2>
        <form onSubmit={handleAddCategory}>
          <div className="form-group">
            <input
              type="text"
              name="name"
              placeholder="Category Name"
              value={newCategory.name}
              onChange={handleInputChange}
              className="form-input"
            />
          </div>
          <div className="form-group">
            <input
              type="file"
              name="file"
              accept="image/*"
              onChange={handleFileChange}
              className="form-input"
            />
          </div>
          <button 
            type="submit" 
            disabled={actionLoading}
            className="btn btn-primary"
          >
            {actionLoading ? "Adding..." : "Add Category"}
          </button>
        </form>
      </div>

      {isLoading ? (
        <div className="loading-spinner">Loading...</div>
      ) : (
        <div className="categories-grid">
          {categories.map((category) => (
            <div key={category.categoryName} className="category-card">
              <img
                src={getImageUrl(category.categoryImagePath)}
                onError={handleImageError}
                alt={category.categoryName}
                className="category-image"
              />
              
              {editingCategory === category.categoryName ? (
                <div className="edit-form">
                  <input
                    type="text"
                    value={editForm.newName}
                    onChange={(e) => setEditForm({...editForm, newName: e.target.value})}
                    placeholder="New Category Name"
                    className="form-input"
                  />
                  <input
                    type="file"
                    onChange={handleEditFileChange}
                    accept="image/*"
                    className="form-input"
                  />
                  <div className="button-group">
                    <button
                      onClick={() => handleUpdateCategory(category.categoryName)}
                      disabled={actionLoading}
                      className="btn btn-success"
                    >
                      {actionLoading ? "Updating..." : "Update"}
                    </button>
                    <button
                      onClick={() => {
                        setEditingCategory(null);
                        setEditForm({ newName: "", newImage: null });
                      }}
                      className="btn btn-secondary"
                    >
                      Cancel
                    </button>
                  </div>
                </div>
              ) : (
                <div className="category-info">
                  <h3>{category.categoryName}</h3>
                  <div className="button-group">
                    <button
                      onClick={() => {
                        setEditingCategory(category.categoryName);
                        setEditForm({
                          newName: category.categoryName,
                          newImage: null
                        });
                      }}
                      className="btn btn-primary"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleDeleteCategory(category.categoryName)}
                      disabled={actionLoading}
                      className="btn btn-danger"
                    >
                      {actionLoading ? "Deleting..." : "Delete"}
                    </button>
                  </div>
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default CategoryManagement;