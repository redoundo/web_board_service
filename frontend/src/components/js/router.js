import { createRouter, createWebHistory } from "vue-router";
import main from "../vues/mainpage.vue";

import question from "../vues/question.vue";
import questionWrite from "../vues/question/writequestion.vue";
import questionUpdate from "../vues/question/updatequestion.vue";
import questionView from "../vues/question/questionview.vue";

import gallery from "../vues/gallery.vue";
import galleryView from "../vues/gallery/galleryview.vue";
import galleryUpdate from "../vues/gallery/updategallery.vue";
import galleryWrite from "../vues/gallery/gallerywrite.vue";

import notify from "../vues/notify.vue";
import notifyView from "../vues/notify/notifyview.vue";

import board from "../vues/board.vue";
import boardView from "../vues/board/boardview.vue";
import boardUpdate from "../vues/board/boardupdate.vue";
import boardWrite from "../vues/board/boardwrite.vue";

import login from "../vues/login/loginpage.vue";
import signIn from "../vues/signIn/signInpage.vue";

const routes = [
	{ path: "/", component: main, name: "main" },
	{ path: "/question", component: question, name: "question" },
	{
		path: "/question/view",
		component: questionView,
		name: "questionView",
		props: true,
	},
	{
		path: "/question/update",
		component: questionUpdate,
		name: "questionUpdate",
		props: true,
	},
	{
		path: "/question/write",
		component: questionWrite,
		name: "questionWrite",
		props: true,
	},
	{ path: "/gallery", component: gallery, name: "gallery" },
	{
		path: "/gallery/view",
		component: galleryView,
		name: "galleryView",
		props: true,
	},
	{
		path: "/gallery/write",
		component: galleryWrite,
		name: "galleryWrite",
		props: true,
	},
	{
		path: "/gallery/update",
		component: galleryUpdate,
		name: "galleryUpdate",
		props: true,
	},
	{ path: "/notify", component: notify, name: "notify" },
	{
		path: "/notify/view",
		component: notifyView,
		name: "notifyView",
		props: true,
	},
	{ path: "/board", component: board, name: "board" },
	{ path: "/board/view", component: boardView, name: "boardView", props: true },
	{
		path: "/board/update",
		component: boardUpdate,
		name: "boardUpdate",
		props: true,
	},
	{
		path: "/board/write",
		component: boardWrite,
		name: "boardWrite",
		props: true,
	},
	{ path: "/login", component: login, name: "login" },
	{ path: "/signIn", component: signIn, name: "signIn" },
];

const router = createRouter({
	history: createWebHistory(),
	routes: routes,
});
export default router;
