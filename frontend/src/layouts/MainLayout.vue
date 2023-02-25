<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated>
      <q-toolbar class="bg-blue text-white shadow-2">
        <q-toolbar-title>retrospecto</q-toolbar-title>
        <q-space/>
        <q-tabs shrink>
          <q-route-tab
            name="home" icon="home"
            to="/"
            exact
          />
          <q-route-tab
            name="board" icon="dashboard"
            to="/board"
            exact
          />
          <q-route-tab
            name="poker" icon="style"
            to="/poker"
            exact
          />
          <q-route-tab
            name="chat" icon="chat"
            to="/chat"
            exact
          />
        </q-tabs>
      </q-toolbar>
    </q-header>
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
</template>

<script>
import {defineComponent, ref} from 'vue'

export default defineComponent({
  name: 'MainLayout',

  components: {},

  setup() {

    return {
      GoToTop() {
        window.scrollTo(0, 0);
      }
    }
  }
})
</script>
