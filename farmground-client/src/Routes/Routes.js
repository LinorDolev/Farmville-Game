import React from "react";
import { Route, Switch } from "react-router-dom";
import Home from "../App/Home/Home";
import Login from "../components/login/Login";
import RegistrationForm from "../components/register/RegistrationForm";
import Playground from "../components/playground/Playground";

export default () =>
    <Switch>
        <Route exact path="/" component={Home} />
        <Route exact path="/login"  component={Login} />
        <Route exact path="/register" component={RegistrationForm} />
        <Route exact path="/playground" component={Playground} />
    </Switch>;