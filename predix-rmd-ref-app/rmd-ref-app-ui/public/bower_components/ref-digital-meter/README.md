Ref-Digital-Meter
-----------------------------------------------

## Overview

Ref-Digital-Meter is a Predix Experience ('Px') component.  It's used in the RMD Reference App UI to show system health.
Here's a screenshot of the component:

<img src="digital-meter.png"></img>

(Of course, it can be resized to fit your needs.)  When the value changes, it will show some animation, filling in the bars.  
If value is low, bars are red, medium - yellow, high - green.
Run the commands below to see it working on a demo page.

## Getting Started

Read https://github.com/PredixDev/px-getting-started.

From the component's directory...

```
$ npm install
$ bower install
$ grunt sass
```

### API and examples

From the component's directory

```
$ grunt depserve
```

Starts a local server. Navigate to the root of that server (e.g. http://localhost:8080/) in a browser to open the API documentation page, with link to the "Demo" / working examples.

### LiveReload

By default grunt depserve is configured to enable LiveReload and will be watching for modifications in your root directory as well as `/css`.

Your browser will also need to have the LiveReload extension installed and enabled. For instructions on how to do this please refer to [livereload.com/extensions/](http://livereload.com/extensions/).

Add, remove, modify file system patterns specified in the `depserve.options.livereload` array in your `Gruntfile.js`

This is an example depserve configuration:

```
depserve: {
    options: {
        open: '&lt;%= depserveOpenUrl %&gt;,
        livereload: [__dirname + "/js", __dirname + "/css", __dirname]
    }
}
```

Disable LiveReload by removing the `livereload` key from the configuration object.

### DevMode

From the component's directory run:

```
$ grunt devmode
```

Starts a local server exactly the same as if you had run `grunt depserve` however in addition it also runs `grunt watch` concurrently which will execute commands on file change according to the specified matching patterns.

This is an example `grunt watch` configuration which watches for changes to SASS files, then on changes executes SASS compilation and automatic prefixing:

```
watch: {
    sass: {
        files: ['sass/**/*.scss'],
        tasks: ['sass', 'autoprefixer'],
        options: {
            interrupt: true
        }
    }
}
```

**We hope `grunt devmode` puts your development into *#beastmode*.**

<img src="http://imgc.allpostersimages.com/images/P-488-488-90/71/7108/JJUV100Z/posters/teen-wolf-beast-mode.jpg" alt="Slam Dunks for Days" width=
"250" />

### Options

Does this component have runtime configuration options?  If so, they should be able to be passed as attributes on the element with examples shown below.

### Function calls

What is the public API of this component?

### Extending styles

Documented CSS extension points?

### Extending behavior

See Polymer composition patterns

GE Coding Style Guide
---------------------

[GE JS Developer's Guide](https://github.com/GeneralElectric/javascript)


### Known Issues
