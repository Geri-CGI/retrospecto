
const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      {path: '/board', component: () => import('pages/BoardPage.vue')},
      {path: '/board/:boardId', component: () => import('pages/BoardPage.vue')},
      {path: '/chat', component: () => import('pages/ChatPage.vue')},
      {path: '/poker', component: () => import('pages/PokerPage.vue')},
      {path: '/poker/room/:roomId', component: () => import('pages/PokerPage.vue')},
      {path: '/', component: () => import('pages/HomePage.vue')},
    ]
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue')
  }
]

export default routes
