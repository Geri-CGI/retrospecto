<template>
  <div class="q-mx-auto">
    <div>
      <q-parallax :height="400" src="~assets/stock.jpg">
        <div :class="getScreenSizeForTitle()" class="text-white text-bold text-center">retrospecto</div>
        <div :class="getScreenSizeForSubTitle()" class="text-white text-center">The best open source
          retrospective tool
        </div>
      </q-parallax>
    </div>
    <div class="row">
      <div class="col-xs-12 col-sm-12 col-md-12 col-lg-4 col-xl-4">
        <q-card class="my-card full-height" flat square>
          <div class="text-center">
            <q-icon name="dashboard" color="blue" size="6.4em" style="padding: 30px"/>
          </div>

          <q-card-section>
            <div class="text-h3 text-center" style="padding-bottom: 20px">Board</div>
            <div class="text-subtitle2 text-center">A retrospective (from Latin retrospectare, "look back"), generally,
              is a look back at events that took place, or works that were produced, in the past. We provide you a board
              to help facilitate it.
            </div>
          </q-card-section>
        </q-card>
      </div>
      <div class="col-xs-12 col-sm-12 col-md-12 col-lg-4 col-xl-4">
        <q-card class="my-card full-height" flat square>
          <div class="text-center">
            <q-icon name="style" color="blue" size="6.4em" style="padding: 30px"/>
          </div>

          <q-card-section>
            <div class="text-h3 text-center" style="padding-bottom: 20px">Poker</div>
            <div class="text-subtitle2 text-center">In planning poker, members of the group make estimates by playing
              numbered cards face-down to the table, instead of speaking them aloud.
            </div>
          </q-card-section>
        </q-card>
      </div>
      <div class="col-xs-12 col-sm-12 col-md-12 col-lg-4 col-xl-4">
        <q-card class="my-card full-height" flat square>
          <div class="text-center">
            <q-icon name="chat" color="blue" size="6.4em" style="padding: 30px"/>
          </div>

          <q-card-section>
            <div class="text-h3 text-center" style="padding-bottom: 20px">Chat</div>
            <div class="text-subtitle2 text-center">Just a simple chat feature for websocket testing with Spring Boot.
              Come and have a talk with anyone. We do not store any history of the chat.
            </div>
          </q-card-section>
        </q-card>
      </div>
    </div>
    <div>
      <q-parallax :height="400" src="~assets/stock.jpg">
        <div :class="getScreenSizeForTitle()" class="text-white text-bold text-center">contribute</div>
        <div :class="getScreenSizeForSubTitle()" class="text-white text-center"><a
          href="https://github.com/Geri-CGI/retrospecto" target="_blank">
          https://github.com/Geri-CGI/retrospecto</a></div>
      </q-parallax>
    </div>
    <div class="row">
      <div class="col">
        <q-card class="my-card full-height" flat square>
          <div class="text-center">
            <q-icon name="help" color="blue" size="6.4em" style="padding: 30px"/>
          </div>
          <q-card-section>
            <div class="text-h3 text-center" style="padding-bottom: 20px">Good to know</div>
            <div class="text-subtitle2 text-center">Retrospecto is not storing any data on a long term. The board, poker
              and chat session are only stored in memory until the session is open by at least one client. We are not
              using any database, so your data is safe.
            </div>
          </q-card-section>
        </q-card>
      </div>
    </div>
  </div>
</template>

<script>
import {defineComponent} from 'vue'
import {Screen} from 'quasar'

export default defineComponent({
  name: 'HomePage',
  created() {
    window.addEventListener("resize", this.getScreenSizeForTitle)
    window.addEventListener("resize", this.getScreenSizeForSubTitle)
  },
  methods: {
    getScreenSizeForTitle() {
      if (Screen.xs) {
        return "text-h4"
      }
      if (Screen.sm) {
        return "text-h3"
      }
      if (Screen.md) {
        return "text-h1"
      }
      if (Screen.lg) {
        return "text-h1"
      }
      if (Screen.xl) {
        return "text-h1"
      }
    },
    getScreenSizeForSubTitle() {
      if (Screen.xs) {
        return "text-h6"
      }
      if (Screen.sm) {
        return "text-h5"
      }
      if (Screen.md) {
        return "text-h3"
      }
      if (Screen.lg) {
        return "text-h3"
      }
      if (Screen.xl) {
        return "text-h3"
      }
    }
  }
})
</script>
