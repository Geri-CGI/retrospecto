<template>
  <div>
    <q-layout view="lHh Lpr lFf">
      <q-header elevated>
        <q-toolbar class="bg-blue text-white shadow-2">
          <q-toolbar-title>retrospecto</q-toolbar-title>
          <q-space/>
          <q-tabs shrink>
            <q-route-tab
              name="home" icon="home"
              to="/"
              exact v-if="!isSmallerScreen()"
            />
            <q-route-tab
              name="board" icon="dashboard"
              to="/board"
              exact v-if="!isSmallerScreen()"
            />
            <q-route-tab
              name="poker" icon="style"
              to="/poker"
              exact v-if="!isSmallerScreen()"
            />
            <q-route-tab
              name="chat" icon="chat"
              to="/chat"
              exact v-if="!isSmallerScreen()"
            />
            <q-btn flat @click="drawer = !drawer" round dense icon="menu" v-if="isSmallerScreen()"/>
          </q-tabs>
        </q-toolbar>
      </q-header>
      <q-drawer
        side="right"
        v-model="drawer"
        bordered
        overlay
        :width="150"
        :breakpoint="400"
      >
        <q-scroll-area class="fit">
          <q-list padding class="menu-list">
            <q-item clickable v-ripple to="/" exact>
              <q-item-section avatar>
                <q-icon name="home"/>
              </q-item-section>

              <q-item-section>
                Home
              </q-item-section>
            </q-item>

            <q-item clickable v-ripple to="/board" exact>
              <q-item-section avatar>
                <q-icon name="dashboard"/>
              </q-item-section>

              <q-item-section>
                Board
              </q-item-section>
            </q-item>

            <q-item clickable v-ripple to="/poker" exact>
              <q-item-section avatar>
                <q-icon name="style"/>
              </q-item-section>

              <q-item-section>
                Poker
              </q-item-section>
            </q-item>

            <q-item clickable v-ripple to="/chat" exact>
              <q-item-section avatar>
                <q-icon name="chat"/>
              </q-item-section>

              <q-item-section>
                Chat
              </q-item-section>
            </q-item>
          </q-list>
        </q-scroll-area>
      </q-drawer>

      <q-footer elevated>
        <q-toolbar>
          <q-toolbar-title>retrospecto</q-toolbar-title>
          <q-space/>
          <q-btn flat icon="navigation" @click="GoToTop()"/>
        </q-toolbar>
      </q-footer>
      <q-page-container>
        <router-view/>
      </q-page-container>
    </q-layout>
  </div>
</template>

<script>
import {defineComponent, ref} from 'vue'
import {Screen} from 'quasar'

export default defineComponent({
  name: 'MainLayout',
  created() {
    window.addEventListener("resize", this.isSmallerScreen)
  },
  methods: {
    isSmallerScreen() {
      return Screen.xs;
    }
  },
  components: {},
  setup() {
    return {
      drawer: ref(false),
      GoToTop() {
        window.scrollTo(0, 0);
      }
    }
  }
})
</script>
