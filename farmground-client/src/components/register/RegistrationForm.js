import React, { Component } from "react";
import { Button, FormGroup, FormControl, ControlLabel } from "react-bootstrap";
import "./RegistrationForm.css";
import UserService from '../../services/UserService'
import {Redirect} from "react-router";

/** email, playground, username, avatar, role */
export default class RegistrationForm extends Component {
    constructor(props) {
        super(props);

        this.state = {
            email: "",
            playground: "",
            username:"",
            avatar:"",
            role:"",
            ok:false,
            goHome:false
        };
    }

    validateForm() {
        // return true;
        return this.state.email.length > 0 && this.state.playground.length > 0 && this.state.username.length > 0 && this.state.avatar.length > 0
            && this.state.role.length > 0 ;
    }

    handleChange = event => {
        this.setState({
            [event.target.id]: event.target.value
        });
        return event
    };

    handleSubmit(event){
        event.preventDefault();

        let registrationForm ={
            email: this.state.email,
            username: this.state.username,
            avatar: this.state.avatar,
            role: this.state.role.toLowerCase(),
            playground: this.state.playground
        };

        console.log("Posting: " + JSON.stringify(registrationForm));

        let getResponse = (res) => {
            this.setState({
                ok: res.ok
            });
        };
        let getBody = (body) => {
            if(this.state.ok){
                this.setState({
                    goHome: true
                });
            }else{
                alert(body.message);
            }
        };

        let userService = new UserService(getResponse, getBody);
        userService.registerUser(registrationForm);
    };

    render() {

        if(this.state.goHome){
            alert("A confirmation email was sent to: " + this.state.email + ", you must confirm your email before logging into FarmGround");
            return <Redirect to='/' />
        }
        return (
            <div className="Registration">
                <form onSubmit={this.handleSubmit.bind(this)}>
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
                    <FormGroup controlId="username" bsSize="large">
                        <ControlLabel>Username</ControlLabel>
                        <FormControl
                            value={this.state.username}
                            onChange={this.handleChange}
                            type="text"
                        />
                    </FormGroup>
                    <FormGroup controlId="avatar" bsSize="large">
                        <ControlLabel>Avatar</ControlLabel>

                        <MultipleOptionsRadio
                            options={"👩‍🌾 👨‍🌾 👩🏻‍🌾 👨🏻‍🌾 👩🏼‍🌾 👨🏼‍🌾 👩🏽‍🌾 👨🏽‍🌾 👩🏾‍🌾 👨🏾‍🌾 👩🏿‍🌾 👨🏿‍🌾".split(" ")}
                            onChange={(value) => this.setState({avatar:value})}
                            size={"40px"}
                        />
                    </FormGroup>
                    <FormGroup controlId="role" bsSize="large">
                        <ControlLabel>Role</ControlLabel>
                        <br/>
                        <MultipleOptionsRadio options={["Player", "Manager"]} onChange={(value) => this.setState({role:value})}/>


                    </FormGroup>
                    <Button
                        block
                        bsSize="large"
                        disabled={!this.validateForm()}
                        // type="submitgg"
                        onClick={this.handleSubmit.bind(this)}
                    >
                        Register
                    </Button>
                </form>
            </div>
        );
    }
}



export class MultipleOptionsRadio extends React.Component{

    constructor(props) {
        super();
        this.state = {
            selected: "option1",
            onChange: props.onChange,
            options: props.options,
            size: props.size ? props.size : "18px"
        }
    }

    handleOptionChangeWithId(changeEvent, value) {
        this.setState({
            selectedOption: changeEvent.target.id,
        });
        this.state.onChange(value);
        return value
    }



    isSelected(value){
        return this.state.selectedOption === value;
    }

    activeIfSelected(value){
        if(this.isSelected(value)){
            return " active";
        }
        return "";
    }

    render(){
        let transforms = this.state.options;

        let options = transforms.map((value, index) => {
            return <label className={"btn btn-secondary"+ this.activeIfSelected("option"+index)} key={index} style={{fontSize: this.state.size}}>
                <input type="radio"
                       name="options"
                       id={"option"+index}
                       checked={this.isSelected("option"+index)}
                       onChange={(event) => this.handleOptionChangeWithId(event, value)}/>
                {value}
            </label>
        });

        return(
            <div className="btn-group btn-group-toggle" data-toggle="buttons">
                {options}
            </div>
        );
    }
}
