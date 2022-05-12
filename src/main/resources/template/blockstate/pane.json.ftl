{
    "multipart": [
        {   "apply": { "model": "${models.pane_post}" }},
        {   "when": { "north": true },
            "apply": { "model": "${models.pane_side}" }
        },
        {   "when": { "east": true },
            "apply": { "model": "${models.pane_side}", "y": 90 }
        },
        {   "when": { "south": true },
            "apply": { "model": "${models.pane_side_alt}" }
        },
        {   "when": { "west": true },
            "apply": { "model": "${models.pane_side_alt}", "y": 90 }
        },
        {   "when": { "north": false },
            "apply": { "model": "${models.pane_noside}" }
        },
        {   "when": { "east": false },
            "apply": { "model": "${models.pane_noside_alt}" }
        },
        {   "when": { "south": false },
            "apply": { "model": "${models.pane_noside_alt}", "y": 90 }
        },
        {   "when": { "west": false },
            "apply": { "model": "${models.pane_noside}", "y": 270 }
        }
    ]
}
