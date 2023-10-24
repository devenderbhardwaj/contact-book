import Route from "./js/utilities/Route.js";
import { Router } from "./js/utilities/Router.js";
import { MainContentArea } from "./js/views/MainContentArea.js";
import { SideBarView } from "./js/views/SideBarView.js";

const root = document.getElementById("root");

root.appendChild(SideBarView.getViewElement());
root.appendChild(MainContentArea.getViewElement());

function logout() {
    const request = new XMLHttpRequest();
    request.open("GET", "/contacts/logout");
    request.send();
    request.onload = () => {
        if (request.status == 200) {
            location.href = request.responseURL;
        }
    }
}
SideBarView.bindOnLogOut(() => logout());

const homeroute = new Route("home", () => {
    document.title = "Contacts";
    MainContentArea.showDefaultView(false);
});

const createContactRoute = new Route("createContact", () => {
    document.title = "Create new Contact";
    MainContentArea.showCreateContact();
});

const contactViewRoute = new Route("contact",
    /** @param {URL} url */
    (url) => {
        document.title = "Contact";
        const id = url.searchParams.get("id");
        const editMode = url.searchParams.get("edit");
        MainContentArea.showContactById({ contact_id: id, editMode: editMode });
    })
Router.setHomeRoute(homeroute);
Router.addRoute(contactViewRoute, createContactRoute);
window.addEventListener("popstate", () => Router.route());
window.addEventListener("DOMContentLoaded", () => Router.route());