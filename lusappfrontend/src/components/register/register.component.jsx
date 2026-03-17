import React, {Component, useEffect, useState} from 'react';
import axios from "axios";
import {BASE_URL} from "../../constants/base_url";

function Register(props) {
    const [error, setError] = useState({
        error: "",
        message: ""
    });
    const {isRegisterMenuEnabled, isRegister} = props;
    const [email, setUserEnmail] = useState('');
    const [displayName, setDisplayName] = useState('');
    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    const [verifyPassword, setVerifyPassword] = useState("");

    const handleEmail = (event) => {
        setUserEnmail(event.target.value);
    }
    const handleDisplayName = (event) => {
        setDisplayName(event.target.value);
    }
    const handlePassword = (event) => {
        setPassword(event.target.value);
    }
    const handleRepeatPassword = (event) => {
        setRepeatPassword(event.target.value);
    }

    useEffect(() => {
        if(repeatPassword !== password) {
            setError({error: "PassNotEqual_error", message: "Passwords do not match"});
        } else if (repeatPassword === null) {
            console.log("aqui");
            setVerifyPassword("")
        } else {
            setVerifyPassword("")
        }
    },[password, repeatPassword]);

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (email === ""){
            setError({error: "email_error", message: "Email Address is required."});
            return;
        } else if(password === ""){
            setError({error: "password_error", message: "Password is required."});
            return;
        } else if(repeatPassword !== password) {
            setError({error: "PassNotEqual_error", message: "Passwords do not match"});
            return;
        } else if(displayName === ""){
            setError({error: "displayName_error", message: "Display name is required."});
        }
        else {
            try {
                const response = await axios.post(`${BASE_URL}/register`, {
                    username: email,
                    password: password,
                    profileName: displayName,

                })

                setError({error: "", message: ""});
                isRegister();
            }catch (error) {
                //setError('This email already exists. Please try again.');
                //setError("error");
                setError({error: "email_error", message: "This email already exists. Please try again."});
            }finally {
                /*          setUserEnmail("");
                            setDisplayName("");
                            setRepeatPassword("");
                            setPassword("");
                            setError({error: "", message: ""});*/
                //setError({error: "", message: ""});
            }
        }
    }


    return (
            <form
                className="register-form position-absolute top-50 start-50 translate-middle p-4"
                hidden={isRegisterMenuEnabled === false}
                onSubmit={handleSubmit}
            >
                <h2 className="create_text">Create an account</h2>
                <div className="email_content p-2">
                    <h5 className="email_text">Email</h5>
                    <input
                        className={`email_input ${error.error === "email_error" ? "error" : ""}`}
                        type="text"
                        value={email}
                        onChange={handleEmail}
                    ></input>
                    {error.error === "email_error" && <p className={"error"}>{error.message}</p>}
                </div>
                <div className="display_content p-2">
                    <h5 className="display_text">Display Name</h5>
                    <input
                        className={`display_input ${error.error === "displayName_error" ? "error" : ""}`}
                        type="text"
                        value={displayName}
                        onChange={handleDisplayName}
                    ></input>
                    {error.error === "displayName_error" && <p className={"error"}>{error.message}</p>}
                </div>
                <div className="password_content p-2">
                    <h5 className="password_text">Password</h5>
                    <input
                        className={`Password_input ${error.error === "password_error" ? "error" : ""}`}
                        type="password"
                        value={password}
                        onChange={handlePassword}
                    ></input>
                    {error.error === "password_error" && <p className={"error"}>{error.message}</p>}
                </div>
                <div className="verify_password_content p-2">
                    <h5 className="verify_password_text">Repeat your password</h5>
                    <input
                        className={`verify_password_input ${error.error === "PassNotEqual_error" ? "error" : ""}`}
                        type="password"
                        value={repeatPassword}
                        onChange={handleRepeatPassword}
                    ></input>
                    {error.error === "PassNotEqual_error" && <p className={"error"}>{error.message}</p>}
                </div>
                <div className="register_btn_container p-2">
                    <button
                        type="submit"
                        className="register_account_btn"
                        /*disabled={true}*/
                    >
                        Register
                    </button>
                    <p className="existing_account py-0 m-0">
                        Have already have an account?
                        <button
                            type="button"
                            className="existing_account_btn"
                            /*onClick={isRegister}*/
                            onClick={() => {
                                setError({error: "", message: ""});
                                setUserEnmail("");
                                setDisplayName("");
                                setRepeatPassword("");
                                setPassword("");
                                isRegister();
                            }}
                        >
                            Login here
                        </button>
                    </p>
                </div>
            </form>
    )
}

export default Register;