import { MainContentArea } from "./js/views/MainContentArea.js";
import { SideBarView } from "./js/views/SideBarView.js";

const root = document.getElementById("root");

root.appendChild(SideBarView.getViewElement());
root.appendChild(MainContentArea.getViewElement());

function logout() {
    const request = new XMLHttpRequest();
    request.open("GET", "logout");
    request.send();
    request.onload = () => {
        if (request.status == 200) {
            location.href = request.responseURL;
        }
    }
}
SideBarView.bindOnContactCreate(() => MainContentArea.createContact());
SideBarView.bindOnLogOut(() => logout());
