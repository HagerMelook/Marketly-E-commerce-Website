import { useState, useEffect } from "react";
import './CategoryManagement.css';
import {
  fetchCategories,
  addCategory,
  updateCategory,
  deleteCategory,
  uploadCategoryImage,
} from "../../components/API/CategoryServiceApi";

const CategoryManagement = () => {
  const [categories, setCategories] = useState([]);
  const [newCategory, setNewCategory] = useState({
    categoryName: "",
    categoryImagePath: "",
  });
  const [editCategory, setEditCategory] = useState(null);
  const [loading, setLoading] = useState(false);

  // Fetch categories on component mount
  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    try {
      setLoading(true);
      const data = await fetchCategories();
      setCategories(data);
    } catch (error) {
      console.error("Error loading categories:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleAddCategory = async () => {
    try {
      setLoading(true);
      await addCategory(newCategory);
      setNewCategory({ categoryName: "", categoryImagePath: "" });
      loadCategories();
    } catch (error) {
      console.error("Error adding category:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateCategory = async () => {
    try {
      setLoading(true);
      await updateCategory(editCategory.categoryName, editCategory);
      setEditCategory(null);
      loadCategories();
    } catch (error) {
      console.error("Error updating category:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteCategory = async (categoryName) => {
    try {
      setLoading(true);
      await deleteCategory(categoryName);
      loadCategories();
    } catch (error) {
      console.error("Error deleting category:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleImageUpload = (e, setCategory) => {
    const file = e.target.files[0];
    if (file) {
      const formData = new FormData();
      formData.append("file", file);

      // Assuming uploadCategoryImage uploads the image to backend and returns the image path
      uploadCategoryImage(newCategory.categoryName, formData)
        .then((imagePath) => {
          setCategory((prev) => ({
            ...prev,
            categoryImagePath: imagePath,
          }));
        })
        .catch((error) => {
          console.error("Error uploading image:", error);
        });
    }
  };

  return (
    <div className="container">
      <h1>Category Management</h1>

      {/* Add New Category */}
      <div className="add-category">
        <h2>Add Category</h2>
        <input
          type="text"
          placeholder="Category Name"
          value={newCategory.categoryName}
          onChange={(e) =>
            setNewCategory((prev) => ({ ...prev, categoryName: e.target.value }))
          }
        />
        <input
          type="file"
          onChange={(e) => handleImageUpload(e, setNewCategory)}
        />
        <button onClick={handleAddCategory} disabled={loading}>
          {loading ? "Adding..." : "Add Category"}
        </button>
      </div>

      {/* Existing Categories */}
      <div className="category-list">
        <h2>Existing Categories</h2>
        {loading && <p>Loading categories...</p>}
        {categories.map((category) => (
          <div key={category.categoryName} className="category-item">
            {editCategory && editCategory.categoryName === category.categoryName ? (
              <div className="edit-category">
                <input
                  type="text"
                  value={editCategory.categoryName}
                  disabled
                />
                <input
                  type="file"
                  onChange={(e) => handleImageUpload(e, setEditCategory)}
                />
                <button onClick={handleUpdateCategory} disabled={loading}>
                  {loading ? "Updating..." : "Save"}
                </button>
                <button onClick={() => setEditCategory(null)}>Cancel</button>
              </div>
            ) : (
              <div className="category-display">
                <img src={category.categoryImagePath} alt={category.categoryName} />
                <h3>{category.categoryName}</h3>
                <button onClick={() => setEditCategory(category)}>Edit</button>
                <button
                  onClick={() => handleDeleteCategory(category.categoryName)}
                  disabled={loading}
                >
                  {loading ? "Deleting..." : "Delete"}
                </button>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default CategoryManagement;
