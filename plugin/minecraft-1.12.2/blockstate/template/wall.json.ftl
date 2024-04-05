{
    "multipart": [
        {   "when": { "up": "true" },
            "apply": { "model": "${wall_post}" }
        },
        {   "when": { "north": "true" },
            "apply": { "model": "${wall_side}", "uvlock": true }
        },
        {   "when": { "east": "true" },
            "apply": { "model": "${wall_side}", "y": 90, "uvlock": true }
        },
        {   "when": { "south": "true" },
            "apply": { "model": "${wall_side}", "y": 180, "uvlock": true }
        },
        {   "when": { "west": "true" },
            "apply": { "model": "${wall_side}", "y": 270, "uvlock": true }
        }
    ]
}
