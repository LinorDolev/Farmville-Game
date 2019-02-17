import React, { Component } from "react";
import { Button, FormGroup, FormControl, ControlLabel } from "react-bootstrap";
import "./Login.css";
import UserService from '../../services/UserService'

import {withRouter} from "react-router-dom";

class Login extends Component {
    constructor(props) {
        super(props);

        this.state = {
            email: "",
            playground: "",
            response:null,
            goToPlayground:false,
            user:null,
        };

    }

    validateForm() {
        return this.state.email.length > 0 && this.state.playground.length > 0;
    }

    handleChange = event => {
        this.setState({
            [event.target.id]: event.target.value
        });
    };

    handleSubmit(event){
        event.preventDefault();
        let credentials = {
            email: this.state.email,
            playground: this.state.playground
        };

        let getRes = (res) => {
            this.setState({response:res});

        };

        let getBody = (body) => {

            if(this.state.response.ok === true) {
                this.setState({
                    goToPlayground: true,
                    user: body
                });

            }else{
                alert("Cannot log in:");
            }
        };

        let userService = new UserService(getRes, getBody);
        userService.loginUser(credentials);
        userService.getElements(credentials).then(body =>{

            if(this.state.goToPlayground) {
                this.props.history.push({
                    pathname: '/playground',
                    state: {
                        user: this.state.user,
                        elements: body
                    }
                });
            }
        });

    };

    render() {

        return (
            <div className="Login">
                <form>
                    <FormGroup controlId="email" bsSize="large">
                        <ControlLabel>Email</ControlLabel>
                        <FormControl
                            autoFocus
                            type="email"
                            value={this.state.email}
                            onChange={this.handleChange}
                        />
                    </FormGroup>
                    <FormGroup controlId="playground" bsSize="large">
                        <ControlLabel>Playground</ControlLabel>
                        <FormControl
                            value={this.state.playground}
                            onChange={this.handleChange}
                            type="text"
                        />
                    </FormGroup>
                    <Button
                        block
                        bsSize="large"
                        disabled={!this.validateForm()}
                        type="button"
                        onClick={this.handleSubmit.bind(this)}
                    >
                        Login
                    </Button>
                </form>
            </div>
        );
    }
}

export default withRouter(Login);