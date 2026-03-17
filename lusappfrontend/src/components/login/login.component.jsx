import React, {useState} from 'react';
import axios from 'axios';
import {BASE_URL} from "../../constants/base_url";


function Login(props) {
    const {onLoginSuccess, isLoggedIn, isRegisterClicked, isLoginMenuEnabled, isRegisterMenuEnabled} = props;
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');



    const handleEmail = (event) => {
        setUsername(event.target.value);
    }
    const handlePassword = (event) => {
        setPassword(event.target.value);
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        setError('');
        try {
            // Faz o pedido POST para o seu backend
            const response = await axios.post(`${BASE_URL}/login`, {
                username: username, // ou email, dependendo do seu modelo
                password: password
            });

            // Se o login for bem-sucedido, a resposta terá o token
            //console.log('Login bem-sucedido!', response.data);

            // PASSO CRUCIAL: Guarde o token no localStorage
            localStorage.setItem('token', response.data);

            // Fetch imediato dos dados do utilizador
/*            const profileRes = await axios.get('http://localhost:8080/profile', {
                headers: { Authorization: `Bearer ${response.data}` }
            });*/
            const profileRes = await axios.get(`${BASE_URL}/profile`, {
                headers: { Authorization: `Bearer ${response.data}` }
            });

            // Passa os dados do user para o App
            onLoginSuccess(profileRes.data);
            /*console.log(profileRes.data);*/

            // Opcional: Redirecione o utilizador para um painel ou página inicial
            // window.location.href = '/dashboard';

        } catch (err) {
            // Se o backend retornar um erro (401, 403, etc.)
            console.error('Falha no login', err);
            setError('Credenciais inválidas. Por favor, tente novamente.');
        } finally {
            setPassword('');

        }
    };

/* position-absolute top-50 start-50 translate-middle p-4*/

    return (
        <form
            className="login"
            hidden={isLoggedIn === true || isLoginMenuEnabled === false}
            onSubmit={handleSubmit}
        >
            <h2 className="Welcome_text">Welcome back!</h2>
            <div className="login_content p-2">
                <h5 className="Login_text">Email</h5>
                <input
                    className="login_input"
                    type="text"
                    value={username}
                    onChange = {handleEmail}
                ></input>
            </div>
            <div className="Password_content p-2">
                <h5 className="Password_text">Password</h5>
                <input
                    className="Password_input"
                    type="password"
                    value={password}
                    onChange={handlePassword}
                ></input>
                {/*<p className="Forgot_password py-0 m-0">Forgot your password?</p>*/}
                <div className="forgot_password_container">
                    <button
                        type="button"
                        className="Forgot_password"
                        /*onClick={}*/
                    >
                        Forgot your password?
                    </button>
                </div>
            </div>
            <div className="btn_container p-2">
                <button
                    type="submit"
                    className="login_btn"
                    onClick={handleSubmit}
                >
                Login
                </button>
                <p className="Register py-0 m-0">
                    Need an account?
                    <button
                        type="button"
                        className="register_btn"
                        onClick={isRegisterMenuEnabled}
                    >
                        Register
                    </button>
                </p>
            </div>
            {error && <p style={{ color: 'red' }}>{error}</p>}
        </form>

    );
}

export default Login;