import React, { Component } from "react";
import { Button, FormGroup, FormControl, ControlLabel } from "react-bootstrap";
import "./SendMessageForm.css";


/** title, content */
export default class SendMessageForm extends Component {
    constructor(props) {
        super(props);

        this.state = {
            user: this.props.user,
            title: "",
            content:"",
            onSubmit: this.props.onSubmit
        };
    }

    validateForm() {
        return this.state.title.length > 0 && this.state.content.length > 0;
    }

    handleChange = event => {
        this.setState({
            [event.target.id]: event.target.value
        });
        return event
    };

    handleSubmit(event){
        this.state.onSubmit(this.state.title, this.state.content);
    };

    render() {
        return (
            <div className="SendMessageForm">
                <form onSubmit={this.handleSubmit.bind(this)}>
                    <FormGroup controlId="title" bsSize="large">
                        <ControlLabel>Title</ControlLabel>
                        <FormControl
                            autoFocus
                            placeholder="Insert a title"
                            type="text"
                            value={this.state.title}
                            onChange={this.handleChange}
                        />
                    </FormGroup>
                    <FormGroup controlId="content" bsSize="large">
                        <ControlLabel >Message Content</ControlLabel>
                        <FormControl
                            componentClass="textarea"
                            placeholder="Type your message here..."
                            value={this.state.content}
                            onChange={this.handleChange}
                            type="text"
                        />
                    </FormGroup>

                    <Button
                        block
                        bsSize="large"
                        disabled={!this.validateForm()}
                        // type="submitgg"
                        onClick={this.handleSubmit.bind(this)}
                    >
                        Publish Message
                    </Button>
                </form>
            </div>
        );
    }
}
