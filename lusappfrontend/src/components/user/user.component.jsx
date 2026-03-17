import React, {useEffect, useRef, useState} from 'react';
import axios from "axios";
import {BASE_URL} from "../../constants/base_url";
import {STRINGS} from "../../constants/strings";
import {VERSION} from "../../constants/appversion";

function User(props){
    const {isPreferencesEnable, onPreferencesChange, userData, onLogout} = props;
    const menuRef = useRef(null);
    const inputName = useRef(null);
    const inputDescription = useRef(null);
    const [aberto, setAberto] = useState(isPreferencesEnable);
    const [showEdit, setShowEdit] = useState(false);
    const [selectedFile, setSelectedFile] = useState(null);
    const [menuType, setMenuType] = useState("profile");
    const [isEditingName, setIsEditingName] = useState(false);
    const [isEditingDescription, setIsEditingDescription] = useState(false);
    const [changeLanguage, setChangeLanguage] = useState(false);
    const [profileName, setProfileName] = useState( "");
    const [profileImage, setProfileImage] = useState( "");
    const [description, setDescription] = useState("");
    const [language, setLanguage] = useState("");
    const [userPicture, setUserPicture] = useState("");
    const [error, setError] = useState('');

    useEffect(() => {
        setAberto(isPreferencesEnable);
    },[isPreferencesEnable]);
    useEffect(() => {
        //setAberto(isPreferencesEnable);
        const handleClickOutside = (event) => {
            if (aberto && menuRef.current && !menuRef.current.contains(event.target)) {
                onPreferencesChange(false);
                setAberto(false);
            }
            if(isEditingName && inputName.current && !inputName.current.contains(event.target)){
                setIsEditingName(false);
                handleEditingName(event);
            }
            if(isEditingDescription && inputDescription.current && !inputDescription.current.contains(event.target)){
                setIsEditingDescription(false);
                handleEditingDescription(event);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    });
    useEffect(() => {
        if (isEditingName && inputName.current) {
            inputName.current.focus();
        } else if (isEditingDescription && inputDescription.current) {
            inputDescription.current.focus();
        }
    }, [isEditingName,isEditingDescription]);
    useEffect(() => {
        if (userData?.name) {
            setProfileName(userData.name);
        }
        if (userData?.description) {
            setDescription(userData?.description);
        }
        if (userData?.profileImage) {
            setProfileImage(userData?.profileImage);
        }
    }, [userData]);
    useEffect(() => {
        if (!userData || !userData.imagePath) return;
        const token = localStorage.getItem("token");
        fetch(`${userData.imagePath}`, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`
            }
        })
            .then(response => response.blob())
            .then(blob => {
                setUserPicture(URL.createObjectURL(blob));
            })
    }, [userData]);
    const onProfilePictureUpload = async (event) => {
        const file = event.target.files[0];
        if (file) {
            const formData = new FormData();
            formData.append("picture", file, file.name);

            try {
                const token = localStorage.getItem("token");
                const response = await axios.post(`${BASE_URL}/upload-profile-picture`,
                    formData,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            "Content-Type": "multipart/form-data",
                        },
                    })
            } catch (error){
                console.log(error);
            }
        }
    }
    const handleEditingName = (event) => {
        if(isEditingName){
            setIsEditingName(false);
            handleUpdateName(event);
        } else
            setIsEditingName(true);
    }
    const handleEditingDescription = (event) => {
        if(isEditingDescription){
            setIsEditingDescription(false);
            handleUpdateDescription(event);
        } else
            setIsEditingDescription(true);
    }
    /*Languages*/
    useEffect(() => {
        if(userData?.language) {
            setLanguage(userData.language);
        }
    }, [userData]);
    const handleEditingLanguage = (event) => {
        const newLang = event.target.value;
        setLanguage(newLang);
        handleUpdatelanguage(event,newLang);
    }
    const handleUpdatelanguage = async (event, newLang) => {
        //event.preventDefault();
        try {
            const token = localStorage.getItem("token");
            const response = await axios.put(`${BASE_URL}/update`,{
                    profileName: "",
                    profileImage: "",
                    language: newLang,
                    description: "",
                },
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
        }catch (error) {}
    }
    /*---------------------------------*/
    const handleUpdateName = async (event) => {
        event.preventDefault();
        try {
            const token = localStorage.getItem("token");
            const response = await axios.put(`${BASE_URL}/update`,{
                profileName: profileName,
                profileImage: "",
                language: "",
                description: ""
            },
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
        }catch (error) {}
    }
    const handleUpdateDescription = async (event) => {
        event.preventDefault();
        try {
            const token = localStorage.getItem("token");
            const response = await axios.put(`${BASE_URL}/update`,{
                    profileName: "",
                    profileImage: "",
                    language: "",
                    description: description,
                },
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
        }catch (error) {}
    }
    /*position-absolute top-50 start-0 p-2*/
    return (

        <div
            ref={menuRef}
            className={`user-card ${isPreferencesEnable ? "open" : ""}`}
        >

            <div className="user-card-sidebar">
                <div className="user-settings-container">
                    <ul className="user-settings">
                        <li className="user-settings-item">
                            {userData && (
                                <div>
                                <button
                                    className="general-button"
                                    onClick={() => {
                                        setMenuType("general")
                                    }}
                                >
                                    <i className="bi bi-laptop"></i>
                                    {/*General*/}
                                    {STRINGS[userData.language].General}
                                </button>
                            </div>
                                )}
                        </li>
                        <li className="user-settings-item">
                            {userData && (
                            <div>
                                <button
                                    className="account-button"
                                    onClick={() => {
                                        setMenuType("account")
                                    }}
                                >
                                    <i className="bi bi-key"></i>
                                    {STRINGS[userData.language].Account}
                                </button>
                            </div>
                                )}
                        </li>
                        <li className="user-settings-item">
                            {userData && (
                            <div>
                                <button
                                    className="appearance-button"
                                    onClick={() => {
                                        setMenuType("appearance")
                                    }}
                                >
                                    <i className="bi bi-brush"></i>
                                    {STRINGS[userData.language].Appearance}
                                </button>
                            </div>
                                )}
                        </li>
                        <li className="user-settings-item">
                            {userData && (
                            <div>
                                <button
                                    className="help-button"
                                    onClick={() => {
                                        setMenuType("help")
                                    }}
                                >
                                    <i className="bi bi-info-circle"></i>
                                    {STRINGS[userData.language].Help}
                                </button>
                            </div>
                                )}
                        </li>
                    </ul>
                </div>
                <div className="user-settings-item">
                    {userData && (
                    <div className="user-settings-item-content">
                        <button
                            className="profile-button"
                            onClick={() => {
                                setMenuType("profile")
                            }}
                        >
                            <i className="bi bi-person-circle"></i>
                            {STRINGS[userData.language].Profile}
                        </button>
                    </div>
                        )}
                </div>
            </div>
            <div className="user-card-container" hidden={menuType !== "profile"}>
                <div className="user-card-settings">
                    <div className="user-picture-container"
                         onMouseEnter={() => {
                             setShowEdit(true);
                         }}
                         onMouseLeave={() => {
                             setShowEdit(false);
                         }}
                    >
                        {showEdit && (<i className="bi bi-pencil"></i>)}
                        {userData && (
                            <div className="change-profile-picture-button">
                                <input
                                    id="profile-picture-upload"
                                    type="file"
                                    style={{display: "none"}}
                                    onChange={onProfilePictureUpload}
                                ></input>
                                <img
                                    className="user-picture"
                                    src={userPicture || null}
                                    alt="">
                                </img>
                                <label className="change-profile-picture" htmlFor="profile-picture-upload"></label>
                            </div>
                        )}
                    </div>
                    <div className="user-card-name">
                        {userData && (
                            isEditingName ? (
                                <form onSubmit={handleEditingName}>
                                    <input
                                        ref={inputName}
                                        maxLength={19}
                                        type="text"
                                        value={profileName}
                                        onChange={(e) => setProfileName(e.target.value)}
                                        className="user-card-name-input"
                                    ></input>
                                </form>

                            ) : (
                                <h4 className="user-name-title">
                                    {profileName}
                                </h4>
                            )
                        )}
                        <button
                            className="user-card-name-button"
                            onClick={handleEditingName}>
                            <i className="bi bi-pencil"></i>
                        </button>
                    </div>

                        <div className="user-card-description">
                            {userData && (
                                <h5 className="user-card-description-subtitle">{STRINGS[userData.language].About}</h5>
                            )}

                            <div className="user-card-description-content">
                                {userData && (
                                    isEditingDescription ? (
                                        <form onSubmit={handleEditingDescription}>
                                            <input
                                                ref={inputDescription}
                                                maxLength={19}
                                                type="text"
                                                value={description}
                                                onChange={(e) => setDescription(e.target.value)}
                                                className="user-card-description-input"
                                            ></input>
                                        </form>
                                    ) : (
                                        <p className="user-description">
                                            {description}
                                        </p>
                                    )
                                )}
                                <button
                                    className="user-card-description-button"
                                    onClick={handleEditingDescription}>
                                <i className="bi bi-pencil"></i>
                                </button>
                            </div>
                        </div>

                </div>
                <div className="user-card-session">
                    {userData && (
                        <button
                            className="logoff-button"
                            onClick={() => {
                                onLogout();
                                setAberto(false);
                                onPreferencesChange(false);
                            }}
                        >
                            {STRINGS[userData.language].LogOff}
                        </button>
                    )}
                </div>
            </div>
            <div className="general-card-container" hidden={menuType !== "general"}>
                {userData && (
                    <div className="general-card-content">
                        <h4 className="general-card-title">{STRINGS[userData.language].General}</h4>
                        <h5 className="general-card-subtitle">{STRINGS[userData.language].Language}</h5>
                        <select className="general-card-language" value={language} onChange={handleEditingLanguage}>
                            <option value="en">{STRINGS[userData.language].English}</option>
                            <option value="fr">{STRINGS[userData.language].French}</option>
                            <option value="es">{STRINGS[userData.language].Spanish}</option>
                            <option value="pt">{STRINGS[userData.language].Portuguese}</option>
                            <option value="de">{STRINGS[userData.language].German}</option>
                            <option value="it">{STRINGS[userData.language].Italian}</option>
                        </select>
                    </div>
                )}
            </div>
            {userData && (
                <div className="account-card-container" hidden={menuType !== "account"}>
                    <div className="account-card-items">
                        <h4 className="account-card-title">{STRINGS[userData.language].Account}</h4>
                        <h5 className="account-card-subtitle">{STRINGS[userData.language].InvitationCode}</h5>
                        <p className="invitation-code">{userData.inviteCode}</p>
                    </div>
                    {userData && (<p>{STRINGS[userData.language].AccountCreated}<span>{userData.createdAt.substr(0,10)}</span></p>)}
                </div>
            )}
            {userData && (
                <div className="appearance-card-container" hidden={menuType !== "appearance"}>
                    <h4>{STRINGS[userData.language].Appearance}</h4>
                </div>
            )}
            {userData && (
                <div className="help-card-container" hidden={menuType !== "help"}>
                    <div className="help-card-content">
                        <h4 className="help-card-title">{STRINGS[userData.language].Help}</h4>
                        <h5 className="help-card-subtitle">LusApp</h5>
                        <p className="help-card-subtitle">{STRINGS[userData.language].Version} {VERSION}</p>
                    </div>
                </div>
            )}
        </div>

    );
}

export default User;