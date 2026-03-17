import React, {useEffect, useRef, useState} from 'react';
import {STRINGS} from "../../constants/strings";
import axios from "axios";
import {BASE_URL} from "../../constants/base_url";

function NewChat (props) {
    const {
        userData,
        newChatMenu,
        menuActionChat,
        newContactMenu,
        pendingContactList,
        contactList,
        contactPictures
    } = props;
    const menuRef = useRef(null);


    useEffect(() => {
        const handleClickOutside = (event) => {
            if (newChatMenu && menuRef.current && !menuRef.current.contains(event.target)) {
                menuActionChat();
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    });
    const handleAcceptInvite = async (inviteID) => {
        //event.preventDefault();

        try {
            const token = localStorage.getItem("token");
            const response = await axios.post(`${BASE_URL}/invite/accept?invite_ID=${inviteID}`,{}, {
                headers: {Authorization: `Bearer ${token}`}
            });

        }catch(error) {

        }
    }
    const handleRejectInvite = async (inviteID) => {
        try {
            const token = localStorage.getItem("token");
            const response = await axios.post(`${BASE_URL}/invite/decline?invite_ID=${inviteID}`,{}, {
                headers: {Authorization: `Bearer ${token}`}
            });

        }catch(error) {

        }
    }

    const handleCreateChat = async (contact) => {
        console.log(contact.id);
        try {
            const token = localStorage.getItem("token");
            const response = await axios.post(`${BASE_URL}/chat`, {
                contactID: contact.id,
            },
            {
                headers: {Authorization: `Bearer ${token}`}
            });
        }catch(error) {

        }
    };
    return(
            <div
                ref={menuRef}
                className="newChat-container"
                hidden={newChatMenu!==true}>
                {userData && (
                    <div className="newChat-items">
                        <h4 className="newChat-title">{STRINGS[userData.language].NewChat}</h4>
                        <form className="newChat-form">
                            <input
                                className="newChat-search"
                                placeholder={STRINGS[userData.language].Search}
                            ></input>
                        </form>
                        <div className="newChat-item">
                            <button className="newChat-newGroup-btn">
                                <i className="bi bi-people"></i>
                                <span className="newChat-label">{STRINGS[userData.language].NewGroup}</span>
                            </button>
                        </div>
                        <div className="newChat-item">
                            <button
                                className="newChat-newContact-btn"
                                onClick={newContactMenu}
                            >
                                <i className="bi bi-person"></i>
                                <span className="newChat-label">{STRINGS[userData.language].NewContact}</span>
                            </button>
                        </div>
                        <p className="newChat-subtitle">{STRINGS[userData.language].PendingContacts}</p>
                        {pendingContactList && pendingContactList.map((invite, index) =>
                            <div key={index} className="pending-contact-item">
                                <div className="pending-contact-item-details">
                                    <img
                                        className="contact-user-picture"
                                        src={invite.profileImage}
                                    ></img>
                                    <div className="pending-contact-item-username">{invite.username}</div>
                                </div>
                                <div className="pending-contact-item-decision">
                                    <button
                                        className="accept-contact-btn"
                                        onClick={() => {
                                            handleAcceptInvite(invite.inviteID);
                                        }
                                    }
                                    >
                                        <i className="bi bi-check-lg"></i>
                                    </button>
                                    <div className="pending-contact-item-divider"></div>
                                    <button
                                        className="reject-contact-btn"
                                        onClick={() => {
                                            handleRejectInvite(invite.inviteID);
                                        }}
                                    >
                                        <i className="bi bi-x-lg"></i>
                                    </button>
                                </div>
                            </div>
                        )}
                        <p className="newChat-subtitle">{STRINGS[userData.language].AllContacts}</p>
                        {contactList && contactList.map((contact, index) =>
                            <div key={index} className="contact-item">
                                <button
                                    key={index}
                                    className="contact-start-chat-btn"
                                    onClick={()=>{
                                        handleCreateChat(contact);
                                        //console.log("chat added " + index +" "+ contact.profileName);
                                    }}
                                >
                                    <div className="contact-item-details">
                                        <img
                                            className="contact-user-picture"
                                            src={contactPictures[contact.id]}
                                        ></img>
                                        <div className="pending-contact-item-username">{contact.profileName}</div>
                                    </div>
                                </button>
                            </div>
                        )}

                    </div>
                )}
            </div>
    );
}

export default NewChat;