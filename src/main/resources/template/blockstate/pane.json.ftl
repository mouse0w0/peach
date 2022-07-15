{
    "multipart": [
        {   "apply": { "model": "${pane_post}" }},
        {   "when": { "north": true },
            "apply": { "model": "${pane_side}" }
        },
        {   "when": { "east": true },
            "apply": { "model": "${pane_side}", "y": 90 }
        },
        {   "when": { "south": true },
            "apply": { "model": "${pane_side_alt}" }
        },
        {   "when": { "west": true },
            "apply": { "model": "${pane_side_alt}", "y": 90 }
        },
        {   "when": { "north": false },
            "apply": { "model": "${pane_noside}" }
        },
        {   "when": { "east": false },
            "apply": { "model": "${pane_noside_alt}" }
        },
        {   "when": { "south": false },
            "apply": { "model": "${pane_noside_alt}", "y": 90 }
        },
        {   "when": { "west": false },
            "apply": { "model": "${pane_noside}", "y": 270 }
        }
    ]
}
