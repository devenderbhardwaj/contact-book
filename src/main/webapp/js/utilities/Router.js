import Route from "./Route.js";

class RouterClass {
    /**
     * @prop
     * @type {Route[]}
     */
    routes = [];

    /**
     * @prop
     * @type {Route}
     */
    homeroute

    
    constructor() {}
    /**
     * 
     * @param {Route[]}
     */
    addRoute(...routes) {
        this.routes = [...this.routes, ...routes];
    }
    /**
     * @param {Route} route
     */
    setHomeRoute(route) {
        this.homeroute = route;
    }
    route() {
        const url = new URL(location.href);
        const route = (this.routes.find( route => url.pathname.replace("/contacts/home/", "").startsWith(route.path))) || this.homeroute;
        route.navigateTo(url);
    }
}

export const Router = new RouterClass();