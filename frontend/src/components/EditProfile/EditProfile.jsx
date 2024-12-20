import { useState, useEffect } from "react";
import "./EditProfile.css";
import Header from "../header/Header";
import * as EditProfileApi from '../../components/API/EditProfileApi';

const UserAccount = () => {
  const [userData, setUserData] = useState(null);
  const [originalData, setOriginalData] = useState(null);

  useEffect(() => {
    const fetchFunction = () => {
        switch (getUserType()) {
            case "admin": return EditProfileApi.fetchAdminInfo(getUserId());
            case "vendor": return EditProfileApi.fetchVendorInfo(getUserId());
            case "client": return EditProfileApi.fetchClientInfo(getUserId());
        }
    }
      fetchFunction().then(adminInfo => {
            if (adminInfo) {
                setUserData(adminInfo);
                setOriginalData(adminInfo);
            } else {
                console.log('Admin info not found or an error occurred.');
            }
        });
    }, []);

  const [editingField, setEditingField] = useState(null);
  const [isModified, setIsModified] = useState(false);

  const getUserType = () => {
    return "client";
  }

  const getUserId = () => {
    return 2; 
  }

  const handleEditClick = (field) => {
    setEditingField(field==editingField ? null : field);
  };

  const handleChange = (field, value) => {
    setUserData((prev) => ({
      ...prev,
      [field]: value,
    }));
    setIsModified(true);
  };

  const handleSave = () => {
    if (!originalData) {
    console.error("Original data is not available.");
    return;
    }

    const updateFunction = (patch) => {
        switch (getUserType()) {
            case "admin": return EditProfileApi.updateAdminInfo(getUserId(), patch);
            case "vendor": return EditProfileApi.updateVendorInfo(getUserId(), patch);
            case "client": return EditProfileApi.updateClientInfo(getUserId(), patch);
        }
    }

    const patch = Object.keys(userData)
    .filter((key) => userData[key] !== originalData[key])
    .map((key) => ({
        op: "replace",
        path: `/${key}`,
        value: userData[key],
    }));

    updateFunction(patch)
    .then(() => {
        setOriginalData(userData);
        setIsModified(false);
    })
    .catch((error) => {
        console.error("Failed to save changes:", error);
    });

    setEditingField(null);
    setIsModified(false);
  };

  return (<> {userData && 
        <>  
            <Header/>
            <div className="account-page">
            <h1>Your Profile</h1>
            <div className="info-list">
                <div className="info-row" >
                    <div className="info-label" style={{color: "black", fontSize: "14pt"}}>Account Information</div>
                </div>

                <div className="info-row" >
                    <div className="info-label">{"Username"}</div>
                    <div className="info-static">{userData.username}</div>
                </div>

                {userData.authType=="oauth" && <div className="info-row" >
                    <div className="info-label">{"Email"}</div>
                    <div className="info-static">{userData.username+"@gmail.com"}</div>
                </div>}

                <div className="info-row" >
                    <div className="info-label">{"Role"}</div>
                    <div className="info-static">{userData.type}</div>
                </div>

                <div className="info-row" >
                    <div className="info-label">{"Account State"}</div>
                    <div className="info-static">{userData.active ? "Active" : "Banned"}</div>
                </div>

                {userData.authType === "basic" && (
                <div className="info-row">
                    <div className="info-label">Password</div>
                    {editingField === "password" ? (
                    <input
                        className="info-input"
                        type="password"
                        value={userData.password}
                        onChange={(e) => handleChange("password", e.target.value)}
                    />
                    ) : (
                    <div className="info-value">{"*".repeat(userData.password.length)}</div>
                    )}
                    {userData.password.length<8 && <span className="too-short">Password is too short.</span>}
                    <button className="edit-button" onClick={() => handleEditClick("password")}>
                    {editingField === "password" ? "Editing" : "Edit"}
                    </button>
                </div>
                )}

                <div className="info-row" >
                    <div className="info-label" style={{color: "black", fontSize: "14pt"}}>{userData.type=="vendor" ? "Business Information" : "Personal Information"}</div>
                </div>

                {(userData.type=="vendor" ? ["organizationName"] : ["firstName", "lastName"]).map((field) => (
                <div className="info-row" key={field}>
                    <div className="info-label">{field.replace(/([A-Z])/g, " $1").replace(/^./, (str) => str.toUpperCase())}</div>
                    {editingField === field ? (
                    <input
                        className="info-input"
                        value={userData[field]}
                        onChange={(e) => handleChange(field, e.target.value)}
                    />
                    ) : (
                    <div className="info-value">{userData[field]}</div>
                    )}
                    <button className="edit-button" onClick={() => handleEditClick(field)}>
                    {editingField == field ? "Editing" : "Edit"}
                    </button>
                </div>
                ))}

                {userData.type=="vendor" && <div className="info-row" >
                    <div className="info-label">Tax Number</div>
                    <div className="info-static">{userData.taxNumber}</div>
                </div>}

                {["address", "phone", "postalCode"].map((field) => (
                <div className="info-row" key={field}>
                    <div className="info-label">{field.replace(/([A-Z])/g, " $1").replace(/^./, (str) => str.toUpperCase())}</div>
                    {editingField === field ? (
                    <input
                        className="info-input"
                        value={userData[field]}
                        onChange={(e) => handleChange(field, e.target.value)}
                    />
                    ) : (
                    <div className="info-value">{userData[field]}</div>
                    )}
                    <button className="edit-button" onClick={() => handleEditClick(field)}>
                    {editingField == field ? "Editing" : "Edit"}
                    </button>
                </div>
                ))}
            </div>
            <button className="save-button" disabled={!isModified || (userData.password.length<8)} onClick={handleSave}>
                Save Changes
            </button>
            </div>
        </>
    }</>
  );
};

export default UserAccount;
