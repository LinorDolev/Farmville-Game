import fetch from 'isomorphic-fetch'


export default class UserService {

    constructor(getResponse=(res) =>{return res;}, getBody=(body)=>{return body;}){
        this.getResponse = getResponse;
        this.getBody = getBody;

         this.url = "http://localhost:8083";
        //this.url = "http://10.100.102.75:8083";

    }


    registerUser(registrationForm){
        let url = this.url + "/playground/users";

        fetch(url,{
            method: 'POST',
            body: JSON.stringify(registrationForm),
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'Access-Control-Allow-Origin':'*',
                'Access-Control-Allow-Methods' :'POST, GET, DELETE, PUT'
            }
        }).then(response => {
            console.log(response);
            this.getResponse(response);
            return response.json();
        }).then(body => {
            console.log(body);
            this.getBody(body);
        });
    }

    loginUser(credentials){
        let url = this.url + "/playground/users/login/"+credentials.playground+"/"+credentials.email;

        /*
        ,{
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Access-Control-Allow-Origin':'*',
                'Access-Control-Allow-Methods' :'POST, GET, DELETE, PUT'
            }
        }
         */
       return fetch(url).then(response => {
            //console.log(response);
            this.getResponse(response);
            return response.json();
        }).then(body => {
            //console.log(body);
            this.getBody(body);
            return body;
        });


    }

    getElements(credentials){
        let url = this.url + "/playground/elements/"+ credentials.playground +"/" + credentials.email +"/all/?size=100&page=0";

        return fetch(url,{
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Access-Control-Allow-Origin':'*',
                'Access-Control-Allow-Methods' :'POST, GET, DELETE, PUT'
            }
        }).then(response => {
            return response.json();
        });

    }


    /**
     *
     private String playground;
     private String id;
     private String elementPlayground;
     private String elementId;
     private String type;
     private String playerPlayground;
     private String playerEmail;
     private Map<String, Object> attributes;
     *
     * */
    plant(credentials, color, name, x, y){
        let url = this.url + "/playground/activities/" +  credentials.playground + "/" + credentials.email;

        let activityTO = {
            playground: credentials.playground,
            id: "",
            elementPlayground: credentials.playground,
            elementId: "",
            type:"Plant",
            playerPlayground: credentials.playground,
            playerEmail: credentials.email,
            attributes: {
                COLOR: color,
                NAME: name,
                X: x,
                Y: y
            }
        };

        return fetch(url,{
            method: 'POST',
            body: JSON.stringify(activityTO),
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'Access-Control-Allow-Origin':'*',
                'Access-Control-Allow-Methods' :'POST, GET, DELETE, PUT'
            }
        }).then(response => {
           // console.log(response);
            this.getResponse(response);
            return response.json();
        }).then(body => {
            //console.log(body);
            return this.getBody(body);
        }).catch((reason => alert(reason)));
    }


    createMessageBoard(credentials){
        let url = this.url + "/playground/elements/" +  credentials.playground + "/" + credentials.email;

        let ElementTO = {
            playground: credentials.playground,
            id: "",
            type:"MessagesBoard",
            name:"Messages Board",
            location:{
                x: 0.0,
                y: 0.0
            },
            creatorPlayground:credentials.playground,
            creatorEmail:credentials.email,
            attributes: {
                visible: true
            },
            creationDate: new Date(),
            expirationDate:""
        };

        return fetch(url,{
            method: 'POST',
            body: JSON.stringify(ElementTO),
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'Access-Control-Allow-Origin':'*',
                'Access-Control-Allow-Methods' :'POST, GET, DELETE, PUT'
            }
        }).then(response => {
            console.log(response);
            this.getResponse(response);
            return response.json();
        }).then(body => {
            console.log(body);
            return this.getBody(body);
        }).catch((reason => alert(reason)));
    }

    writeMessage(credentials, message, messageBoardId){
        let url = this.url + "/playground/activities/" +  credentials.playground + "/" + credentials.email;

        let activityTO = {
            playground: credentials.playground,
            id: "",
            elementPlayground: credentials.playground,
            elementId: messageBoardId,
            type:"WriteMessage",
            playerPlayground: credentials.playground,
            playerEmail: credentials.email,
            attributes: {
                MESSAGE: message
            }
        };

        return fetch(url,{
            method: 'POST',
            body: JSON.stringify(activityTO),
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'Access-Control-Allow-Origin':'*',
                'Access-Control-Allow-Methods' :'POST, GET, DELETE, PUT'
            }
        }).then(response => {
            console.log(response);
            this.getResponse(response);
            return response.json();
        }).then(body => {
            console.log(body);
            return this.getBody(body);
        }).catch((reason => alert(reason)));
    }

    readMessages(credentials, messageBoardId){
        let url = this.url + "/playground/activities/" +  credentials.playground + "/" + credentials.email;

        let activityTO = {
            playground: credentials.playground,
            id: "",
            elementPlayground: credentials.playground,
            elementId: messageBoardId,
            type:"ReadRecentMessages",
            playerPlayground: credentials.playground,
            playerEmail: credentials.email,
            attributes: {
                messages: {
                    message1: [{
                        content:"",
                        title: "",
                        creationTime: ""}]
                }
            }
        };

        return fetch(url,{
            method: 'POST',
            body: JSON.stringify(activityTO),
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'Access-Control-Allow-Origin':'*',
                'Access-Control-Allow-Methods' :'POST, GET, DELETE, PUT'
            }
        }).then(response => {
            // console.log(response);
            this.getResponse(response);
            return response.json();
        }).then(body => {
            //console.log(body);
            return this.getBody(body);
        }).catch((reason => alert(reason)));
    }
}