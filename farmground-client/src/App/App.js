import React, { Component } from "react";
import { Link } from "react-router-dom";
import { Navbar } from "react-bootstrap";
import "./App.css";
import Routes from "../Routes/Routes";
import { BrowserRouter } from 'react-router-dom'

class App extends Component {
    render() {
        return (
            <div id={"whole"}>
            <div>
                <BrowserRouter>
                    <div>
                        <Navbar className={"fluid collapseOnSelect"}>

                            <Navbar.Header>
                                <Navbar.Brand>
                                    <Link className="btn btn-success" to={"/"}>
                                            <span className={"info1"}>Farm</span>
                                            <span className={"info2"}>Ground</span>
                                    </Link>
                                </Navbar.Brand>

                                <Navbar.Brand>
                                    <Link className="btn btn-warning" to={"/login"}>Login</Link>
                                </Navbar.Brand>
                                <Navbar.Brand>
                                    <Link className="btn btn-primary" to={"/register"}>Sign-Up</Link>
                                </Navbar.Brand>
                            </Navbar.Header>
                        </Navbar>

                    <Routes/>
                    </div>
                </BrowserRouter>
            </div>
            </div>
        );
    }
}

export default App;