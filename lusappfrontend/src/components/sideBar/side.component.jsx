import React, {useEffect, useState} from 'react';


function Side(props){
    const {onPreferencesChange, sideContactsEnabled, userData} = props;
    const [options, setOptions] = useState(false);
    const [userPicture, setUserPicture] = useState("");

    const handleOptions = (event) => {
        const newChange = !options;
        setOptions(newChange);
        onPreferencesChange(newChange);
    }
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
    return (
        <div className="sidebar">
            <button
                type="button"
                className="sidebar-item"
                onClick={sideContactsEnabled}
            >
                <span className="Menu-icon">
                    <i className="bi bi-list"></i>
                </span>
            </button>
            <div className="options">
                <button
                    type="button"
                    className="sidebar-item"
                    >
                <span className="Preferences-icon">
                    <i className="bi bi-gear"></i>
                </span>
                </button>
                <button
                    className="sidebar-item"
                    type="button"
                    onClick={handleOptions}
                >
                <span className="User-icon">
                    <img
                        className="profile-picture"
                        src={userPicture || null}
                    ></img>
                </span>
                </button>
            </div>
        </div>
    )
}

export default Side;