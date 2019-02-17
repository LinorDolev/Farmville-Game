import React from 'react';

import './MessageBoard.css';
import * as moment from 'moment';

export default class MessageBoard extends React.Component {

    constructor(props){
        super(props);
        this.props = props;

        this.state ={
            messages: this.props.messages ? this.props.messages : [
                {
                    title: "Welcome to Farmground!",
                    creationTime: "10:24",
                    content:"We wish you a nice farming experience!"
                },
                {
                    title: "Hello Again!",
                    creationTime: "14:10",
                    content:"Its good to see your'e back!"
                },
                {
                    title: "Good job on the tomatoes!",
                    creationTime: "20:10",
                    content:"We see your hard work and appreciate it, continue this way"
                }]
        }
    }


    messagesToItems(){
        let messages = this.state.messages;

        return messages.map((message, index) =>{
            return(<p  key={index} className="list-group-item list-group-item-action flex-column align-items-start">
                        <div className="d-flex w-100 justify-content-between">
                            <h5 className="mb-1"> {message.title} </h5>
                            <small>{moment(message.creationTime,"x").fromNow()}</small>
                        </div>
                        <p className="mb-1"> {message.content} </p>
                        <small>Sent by FarmGround management</small>
                    </p>);
        });

    }


    render() {
        return (
                <div id="messageBoard" className="list-group">
                    {this.messagesToItems()}
                </div>
        );
    }
}
