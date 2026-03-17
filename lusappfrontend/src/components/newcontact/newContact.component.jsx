import React, {useEffect, useRef, useState} from 'react';
import {STRINGS} from "../../constants/strings";
import axios from "axios";
import {BASE_URL} from "../../constants/base_url";

function NewContact(props){
    const {userData, newContact,menuOpen} = props;
    const [inviteCode, setInviteCode] = useState("");

    const handleInviteCode = (event) => {
        setInviteCode(event.target.value);
    }

    const handleInviteContact = async (event) => {
        event.preventDefault();
        try {
            const token = localStorage.getItem("token");
            const response = await axios.post(`${BASE_URL}/invite`, {
                    inviteCode: inviteCode,
                },
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
        } catch (error) {
            console.log(error);
        }
    }


    return (
        <div
            className="newContact-container" hidden={newContact === false}>
            {userData && (
                <div className="newContact">
                    <div className="newContact-title-container">
                        <button
                            className="newContact-back-btn"
                            onClick={menuOpen}
                        >
                            <i className="bi bi-arrow-left"></i>
                        </button>
                        <h4 className="newContact-title">{STRINGS[userData.language].NewContact}</h4>
                    </div>

                    <form
                        className="newContactForm"
                        onSubmit={handleInviteContact}
                    >
                        <input
                            className="newContact-search"
                            type="text"
                            placeholder={STRINGS[userData.language].InvitationCode}
                            value={inviteCode}
                            onChange={handleInviteCode}
                        ></input>
                    </form>
                </div>
            )}
        </div>
    );
}

export default NewContact;