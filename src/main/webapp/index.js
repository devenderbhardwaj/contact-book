import { MainContentArea } from "./js/views/MainContentArea.js";
import { SideBarView } from "./js/views/SideBarView.js";

const root = document.getElementById("root");

root.appendChild(SideBarView.getViewElement());
root.appendChild(MainContentArea.getViewElement());

SideBarView.bindOnContactCreate(() => MainContentArea.createContact());
