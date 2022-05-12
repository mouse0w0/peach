{
    "multipart": [
        {   "apply": { "model": "${models.fence_post}" }},
        {   "when": { "north": "true" },
            "apply": { "model": "${models.fence_side}", "uvlock": true }
        },
        {   "when": { "east": "true" },
            "apply": { "model": "${models.fence_side}", "y": 90, "uvlock": true }
        },
        {   "when": { "south": "true" },
            "apply": { "model": "${models.fence_side}", "y": 180, "uvlock": true }
        },
        {   "when": { "west": "true" },
            "apply": { "model": "${models.fence_side}", "y": 270, "uvlock": true }
        }
    ]
}