<template>
  <q-layout view="lHh Lpr lFf">
    <q-toolbar class="bg-blue text-white shadow-2">
      <q-btn flat label="Retrospecto"/>
      <q-space/>

      <!--
        notice shrink property since we are placing it
        as child of QToolbar
      -->
      <q-tabs shrink>
        <q-route-tab
          name="board" label="Board"
          to="/board"
          exact
        />
        <q-route-tab
          name="poker" label="Poker"
          to="/poker"
          exact
        />
        <q-route-tab
          name="chat" label="Chat"
          to="/chat"
          exact
        />
      </q-tabs>
    </q-toolbar>
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

    return {}
  }
})
</script>
