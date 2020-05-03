import Vue from 'vue'
import Vuetify from 'vuetify/lib'

Vue.use(Vuetify)

export default new Vuetify({
  theme: {
    options: {
      customProperties: true
    },
    themes: {
      light: {
        primary: '#3f51b5',
        secondary: '#c51162',
        accent: '#1faa00',
        error: '#e53935',
        info: '#3f51b5',
        success: '#1faa00',
        snackbarSuccess: '#1faa00',
        warning: '#c51162',
        graphBackground: 'white',
        graphFailedOrUnbenchmarked: 'dimgrey',
        graphReferenceElements: 'grey',
        rowHighlight: '#d3d3d3'
      }
    }
  },
  icons: {
    iconfont: 'mdiSvg'
  }
})
