import { useState, useEffect } from "react";
import { fetchCategories, addCategory, updateCategory, deleteCategory, uploadImage } from "../../components/API/CategoryServiceApi";
import "./CategoryManagement.css";

function CategoryManager() {
  const [categories, setCategories] = useState([]);
  const [newCategory, setNewCategory] = useState({ name: "", imagePath: "" });
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState("");
  const [editingCategory, setEditingCategory] = useState(null);
  const [newCategoryName, setNewCategoryName] = useState("");
  const [newCategoryImagePath, setNewCategoryImagePath] = useState("");

  // Fetch categories function
  const fetchCategoriesData = () => {
    setIsLoading(true);
    fetchCategories()
      .then((fetchedCategories) => {
        setCategories(fetchedCategories);
        setError("");
      })
      .catch((err) => setError(err.message))
      .finally(() => setIsLoading(false));
  };

  // Fetch categories on load
  useEffect(() => {
    fetchCategoriesData();
  }, []);

  // Handle new category input
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewCategory({ ...newCategory, [name]: value });
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setNewCategory({ ...newCategory, imagePath: file.name });
    }
  };

  // Add category
  const handleAddCategory = async (e) => {
    e.preventDefault();
    const addedCategory = {
      categoryName: newCategory.name,
      categoryImagePath: newCategory.imagePath, // This will hold the full path after upload
    };

    try {
      const image = e.target.image.files[0];
      if (image) {
        // Upload the image and get the full path from the backend
        const uploadedImagePath = await uploadImage(image, newCategory.name);
        addedCategory.categoryImagePath = uploadedImagePath;

        // Add the category with the uploaded image's path
        await addCategory(addedCategory);
        fetchCategoriesData();
        setNewCategory({ name: "", imagePath: "" });
        setError(""); // Clear error
      }
    } catch (err) {
      setError(err.message);
    }
  };

  // Update category
  const handleUpdateCategory = async (categoryName, updatedName, updatedImagePath) => {
    const updatedCategory = {
      categoryName: updatedName,
      categoryImagePath: updatedImagePath, // This will hold the full path after upload
    };

    try {
      const image = document.getElementById('image-input').files[0];
      if (image) {
        // Upload the image and get the full path from the backend
        const uploadedImagePath = await uploadImage(image, updatedName);
        updatedCategory.categoryImagePath = uploadedImagePath;
      }

      await updateCategory(categoryName, updatedCategory);
      fetchCategoriesData();
      setEditingCategory(null);
      setNewCategory({ name: "", imagePath: "" });
      setError(""); // Clear error
    } catch (err) {
      setError(err.message);
    }
  };

  // Delete category
  const handleDeleteCategory = (categoryName) => {
    deleteCategory(categoryName)
      .then(() => {
        fetchCategoriesData();
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
          <button type="submit" disabled={isLoading}>Add Category</button>
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

                {/* If editing, show input fields to update the category */}
                {editingCategory === category.categoryName ? (
                  <div>
                    <input
                      type="text"
                      value={newCategoryName}
                      onChange={(e) => setNewCategoryName(e.target.value)}
                      placeholder="New Category Name"
                    />
                    <input
                      id="image-input"
                      type="file"
                      onChange={(e) => {
                        const file = e.target.files[0];
                        if (file) {
                          setNewCategoryImagePath(file.name);
                        }
                      }}
                    />
                    <div className="update-cancel-buttons">
                      <button
                        onClick={() =>
                          handleUpdateCategory(
                            category.categoryName,
                            newCategoryName,
                            newCategoryImagePath
                          )
                        }
                      >
                        Update
                      </button>
                      <button
                        onClick={() => {
                          setEditingCategory(null);
                          setNewCategoryName("");
                          setNewCategoryImagePath("");
                        }}
                      >
                        Cancel Update
                      </button>
                    </div>
                  </div>
                ) : (
                  <button
                    onClick={() => {
                      setEditingCategory(category.categoryName);
                      setNewCategoryName(category.categoryName);
                      setNewCategoryImagePath(category.categoryImagePath);
                    }}
                  >
                    Edit
                  </button>
                )}

                {/* Delete button for category */}
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
