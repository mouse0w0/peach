{
    "multipart": [
        {   "when": { "up": "true" },
            "apply": { "model": "${models.wall_post}" }
        },
        {   "when": { "north": "true" },
            "apply": { "model": "${models.wall_side}", "uvlock": true }
        },
        {   "when": { "east": "true" },
            "apply": { "model": "${models.wall_side}", "y": 90, "uvlock": true }
        },
        {   "when": { "south": "true" },
            "apply": { "model": "${models.wall_side}", "y": 180, "uvlock": true }
        },
        {   "when": { "west": "true" },
            "apply": { "model": "${models.wall_side}", "y": 270, "uvlock": true }
        }
    ]
}
